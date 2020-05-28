package com.bomberman.entity.mapobject.movable

import com.bomberman.BASE_BOMB_RANGE
import com.bomberman.BASE_PLAYER_SPEED
import com.bomberman.BOMB_DURATION
import com.bomberman.MAP_HEIGHT
import com.bomberman.MAX_PLAYER_SPEED
import com.bomberman.PLAYER_SPEED_INCREASE_VALUE
import com.bomberman.PLAYER_TEXTURE_HEIGHT
import com.bomberman.PLAYER_TEXTURE_WIDTH
import com.bomberman.entity.Direction
import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.MapObject
import com.bomberman.game.GameState
import com.bomberman.game.isOutOfBound
import com.bomberman.game.willCollide

data class Player(
        val id: String,
        val bombDurationInSeconds: Int = BOMB_DURATION,
        var speed: Float = BASE_PLAYER_SPEED,
        var bombRange: Int = BASE_BOMB_RANGE,
        var placedBombCount: Int = 0,
        var maxBombCount: Int = 1,
        private val gameState: GameState,
        var dead: Boolean = false,
        var killCount: Int = 0,
        var direction: Int = Direction.IDLE.value,
        override var position: Position
) : MapObject(position), Movable {

  override fun move(position: Position) {
    val obstacles = listOf(gameState.walls, gameState.bricks,
        gameState.bombs.filter { it.hasLeftBombZone(id) }).flatten()

    val currentOnBomb = gameState.bombs.firstOrNull { !it.hasLeftBombZone(id) }

    currentOnBomb?.takeIf { !this.willCollide(position, it) }?.setHasLeftBombZone(id)

    if (this.isOutOfBound(position) || this.willCollide(position, obstacles)) {
      return
    }

    this.position = position
  }

  override val textureHeight: Int
    get() = PLAYER_TEXTURE_HEIGHT

  override val textureWidth: Int
    get() = PLAYER_TEXTURE_WIDTH

  fun increasePlayerSpeed() {
    if (speed >= MAX_PLAYER_SPEED) {
      return
    }
    speed += PLAYER_SPEED_INCREASE_VALUE
    println("Player speed increased to $speed")
  }

  fun decreasePlayerSpeed() {
    if (speed <= 1f) {
      return
    }
    speed -= PLAYER_SPEED_INCREASE_VALUE
    println("player speed decreased to $speed")
  }

  fun increaseBombRange() {
    bombRange += 1
    println("bomb range increased to $bombRange")
  }

  fun decreaseBombRange() {
    if (bombRange <= 0) {
      return
    }
    bombRange -= 1
    println("bomb range decreased to $bombRange")
  }

  fun increaseMaxBombCount() {
    maxBombCount++
    println("max bomb count increased to $maxBombCount")
  }

  fun incrementPlacedBombCount() {
    placedBombCount++
  }

  fun decrementPlacedBombCount() {
    placedBombCount--
  }

  fun canPlaceBomb(): Boolean {
    return placedBombCount < maxBombCount
  }

  fun respawn() {
    position = Position(0, MAP_HEIGHT - PLAYER_TEXTURE_HEIGHT)
    dead = false
    println("Player $id resurrected")
  }

  fun killedEnemy() {
    killCount++
  }

  fun killedHimself() {
    killCount--
  }

  override fun toString(): String {
    return "Player(id='$id', bombDurationInSeconds=$bombDurationInSeconds, speed=$speed, bombRange=$bombRange, placedBombCount=$placedBombCount, maxBombCount=$maxBombCount, dead=$dead, killCount=$killCount, position=$position)"
  }


}