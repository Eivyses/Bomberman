package com.bomberman.entity.mapobject

import com.bomberman.TEXTURE_SIZE
import com.bomberman.entity.BombPlacedZone
import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.movable.Player
import com.bomberman.game.GameState
import java.util.UUID

data class Wall(override var position: Position) : MapObject(position) {
  override val textureWidth: Int
    get() = TEXTURE_SIZE
  override val textureHeight: Int
    get() = TEXTURE_SIZE
}

data class Brick(override var position: Position) : MapObject(position) {
  override val textureWidth: Int
    get() = TEXTURE_SIZE
  override val textureHeight: Int
    get() = TEXTURE_SIZE
}

data class Bomb(
    val bombId: String = UUID.randomUUID().toString(),
    val explosionTime: Int,
    val playerId: String,
    val bombRange: Int,
    val bombPlacedZoneList: List<BombPlacedZone>,
    override var position: Position) : MapObject(position) {
  override val textureWidth: Int
    get() = TEXTURE_SIZE
  override val textureHeight: Int
    get() = TEXTURE_SIZE

  constructor(position: Position, explosionTime: Int, player: Player, players: List<Player>)
      : this(
      position = position,
      explosionTime = explosionTime,
      playerId = player.id,
      bombRange = player.bombRange,
      bombPlacedZoneList =
      players.map { BombPlacedZone(it.id, false) })

  fun setHasLeftBombZone(playerId: String) {
    val player = bombPlacedZoneList.firstOrNull { it.playerId == playerId }
    player?.hasLeftBombZone = true
  }

  fun hasLeftBombZone(playerId: String): Boolean =
      bombPlacedZoneList.firstOrNull { it.playerId == playerId }?.hasLeftBombZone ?: false

  fun bombExplosions(originalBombId: String, playerId: String,
      gameState: GameState): List<BombExplosion> {
    val bombExplosions = mutableListOf<BombExplosion>()
    bombExplosions += BombExplosion(originalBombId, playerId, position)

    for (x in 1..bombRange) {
      val position = Position(position.x + (x * textureWidth), position.y)

      if (position.willHitObstacle(gameState.walls)) {
        break
      }
      if (position.willHitObstacle(gameState.bricks)) {
        bombExplosions += BombExplosion(originalBombId, playerId, position)
        break
      }
      bombExplosions += BombExplosion(originalBombId, playerId, position)
    }

    for (x in 1..bombRange) {
      val position = Position(position.x - (x * textureWidth), position.y)

      if (position.willHitObstacle(gameState.walls)) {
        break
      }
      if (position.willHitObstacle(gameState.bricks)) {
        bombExplosions += BombExplosion(originalBombId, playerId, position)
        break
      }
      bombExplosions += BombExplosion(originalBombId, playerId, position)
    }

    for (y in 1..bombRange) {
      val position = Position(position.x, position.y + (y * textureHeight))

      if (position.willHitObstacle(gameState.walls)) {
        break
      }
      if (position.willHitObstacle(gameState.bricks)) {
        bombExplosions += BombExplosion(originalBombId, playerId, position)
        break
      }
      bombExplosions += BombExplosion(originalBombId, playerId, position)
    }

    for (y in 1..bombRange) {
      val position = Position(position.x, position.y - (y * textureHeight))

      if (position.willHitObstacle(gameState.walls)) {
        break
      }
      if (position.willHitObstacle(gameState.bricks)) {
        bombExplosions += BombExplosion(originalBombId, playerId, position)
        break
      }
      bombExplosions += BombExplosion(originalBombId, playerId, position)
    }

    return bombExplosions
  }

  private fun Position.willHitObstacle(obstacles: List<MapObject>): Boolean =
      obstacles.any { it.position == this }
}


data class BombExplosion(
    val bombId: String,
    val playerId: String,
    override var position: Position) : MapObject(position) {
  override val textureWidth: Int
    get() = TEXTURE_SIZE
  override val textureHeight: Int
    get() = TEXTURE_SIZE
}