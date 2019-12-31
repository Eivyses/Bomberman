package com.bomberman.game

import com.bomberman.PlayerAlreadyExistsException
import com.bomberman.entity.Direction
import com.bomberman.entity.mapobject.Bomb
import com.bomberman.entity.mapobject.Brick
import com.bomberman.entity.mapobject.movable.Player
import com.bomberman.entity.mapobject.pickup.BombPickup
import com.bomberman.entity.mapobject.pickup.Pickup
import com.bomberman.factory.LevelFactory
import com.bomberman.factory.PickupFactory
import com.bomberman.factory.PlayerFactory

class Game(
    private val playerFactory: PlayerFactory,
    private val pickupFactory: PickupFactory,
    val gameState: GameState
) {

  constructor() : this(LevelFactory().createLevel())
  constructor(gameState: GameState) : this(PlayerFactory(), PickupFactory(), gameState)

  fun addPlayer(playerId: String) {
    if (playerExists(playerId)) {
      throw PlayerAlreadyExistsException(playerId)
    }

    val newPlayer = playerFactory.createPlayer(playerId, gameState)
    gameState.players.add(newPlayer)
  }

  fun playerExists(id: String): Boolean =
      gameState.players.any { it.id == id }

  fun removePlayer(id: String) {
    gameState.players.removeIf { it.id == id }
  }

  private fun getPlayer(playerId: String): Player =
      gameState.players.first { it.id == playerId }

  fun movePlayer(playerId: String, direction: Direction) {
    val player = getPlayer(playerId)

    if (player.dead) {
      return
    }

    val movementSpeed = player.speed
    val position = direction.buildPosition(player.position, movementSpeed)
    player.move(position)

    val collidePickup = player.firstPickupCollision(position, gameState.pickups)
    collidePickup?.applyAndRemove(player)

    val explosion = gameState.bombExplosions.firstOrNull { player.willCollide(player.position, it) }

    explosion?.let { updateDeathsAndScore(it.playerId, player) }
  }

  private fun updateDeathsAndScore(killerId: String, player: Player) {
    val killer = gameState.players.firstOrNull { it.id == killerId }
    killer?.updateDeathsAndScore(player)
  }

  fun placeBomb(playerId: String): Bomb? {
    val player = getPlayer(playerId)
    if (!player.canPlaceBomb()) {
      return null
    }

    val position = player.position.round()
    val bomb = Bomb(position, player.bombDurationInSeconds, player, gameState.players)
    if (bomb.willCollide(position, gameState.bombs)) {
      return null
    }
    gameState.bombs += bomb
    player.incrementPlacedBombCount()
    return bomb
  }

  fun explodeBomb(bomb: Bomb, bombId: String, playerId: String) {
    if (bomb !in gameState.bombs) {
      return
    }
    val player = getPlayer(playerId)
    player.decrementPlacedBombCount()
    gameState.bombs -= bomb

    val bombsInExplosionsRange = gameState.bombs
        .filter { it.isInExplosionRange(bomb.bombRange, bomb, gameState.collideObjects) }

    gameState.bombExplosions += bomb.bombExplosions(bombId, playerId, gameState)
    bombsInExplosionsRange.forEach { println(it) }
    bombsInExplosionsRange.forEach { explodeBomb(it, bombId, playerId) }
  }

  fun respawnPlayer(playerId: String) {
    getPlayer(playerId).respawn()
  }

  fun checkKilled(bomb: Bomb) {
    val playersInExplosionRange = gameState.players.filter {
      it.willCollide(it.position, gameState.bombExplosions)
    }
    val killer = getPlayer(bomb.playerId)
    playersInExplosionRange.forEach { killer.updateDeathsAndScore(it) }
  }

  private fun Player.updateDeathsAndScore(killedPlayer: Player) {
    if (killedPlayer.dead) {
      return
    }
    if (this.id == killedPlayer.id) {
      this.killedHimself()
      this.dead = true
      println("Player ${this.id} killed himself")
    } else {
      this.killedEnemy()
      killedPlayer.dead = true
      println("Player ${killedPlayer.id} died from player ${this.id}")
    }
  }

  fun removeExplosions(bomb: Bomb) {
    val explosions = gameState.bombExplosions.filter { it.bombId == bomb.bombId }

    val bricksInExplosion = gameState.bricks.filter { brick ->
      explosions.any { explosion ->
        brick.position == explosion.position
      }
    }

    bricksInExplosion
        .map { it.createPickupAndRemoveBrick() }
        .forEach { gameState.pickups += it }

    gameState.bombExplosions -= explosions
  }

  fun increasePlayerSpeed(playerId: String) {
    getPlayer(playerId).increasePlayerSpeed()
  }

  fun decreasePlayerSpeed(playerId: String) {
    getPlayer(playerId).decreasePlayerSpeed()
  }

  fun increaseBombRange(playerId: String) {
    getPlayer(playerId).increaseBombRange()
  }

  fun decreaseBombRange(playerId: String) {
    getPlayer(playerId).decreaseBombRange()
  }

  fun increaseMaxBombCount(playerId: String) {
    getPlayer(playerId).increaseMaxBombCount()
  }

  private fun Brick.createPickupAndRemoveBrick(): BombPickup {
    gameState.bricks.remove(this)
    return pickupFactory.createBombPickup(this)
  }

  private fun Pickup.applyAndRemove(player: Player) {
    this.apply(player)
    if (this is BombPickup) {
      gameState.pickups.remove(this)
    }
  }

}