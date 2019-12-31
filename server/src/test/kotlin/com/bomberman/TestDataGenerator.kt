package com.bomberman

import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.Wall
import com.bomberman.entity.mapobject.movable.Player
import com.bomberman.game.GameState
import java.security.InvalidParameterException

class TestDataGenerator(
    val gameState: GameState = GameState()
) {

  fun getPlayer(playerId: String): Player =
      gameState.players.first { it.id == playerId }

  fun createPlayerAtColRow(playerId: String, col: Int, row: Int): TestDataGenerator =
      createPlayerAtPosition(playerId, col * TEXTURE_SIZE, row * TEXTURE_SIZE)

  fun createPlayerAtPosition(playerId: String, x: Int, y: Int): TestDataGenerator =
      createPlayerAtPosition(playerId, x.toFloat(), y.toFloat())

  fun createPlayerAtPosition(playerId: String, x: Float, y: Float): TestDataGenerator {
    checkBoundaries(x, y);
    gameState.players += Player(
        id = playerId,
        position = Position(x, y),
        gameState = gameState
    )
    return this
  }

  fun createWallAtColRow(col: Int, row: Int): TestDataGenerator {
    return createWallAtPosition(col * TEXTURE_SIZE, row * TEXTURE_SIZE)
  }

  fun createWallAtPosition(x: Int, y: Int): TestDataGenerator =
      createWallAtPosition(x.toFloat(), y.toFloat())

  fun createWallAtPosition(x: Float, y: Float): TestDataGenerator {
    checkBoundaries(x, y)
    gameState.walls += Wall(Position(x, y))
    return this
  }

  private fun checkBoundaries(x: Float, y: Float) {
    if (x > MAP_WIDTH) {
      throw InvalidParameterException("Position X can't be more than: $MAP_WIDTH")
    }
    if (y > MAP_HEIGHT) {
      throw InvalidParameterException("Position Y can't be more than $MAP_HEIGHT")
    }
  }
}

fun positionAtColRow(col: Int, row: Int): Position =
    Position(col * TEXTURE_SIZE, row * TEXTURE_SIZE)