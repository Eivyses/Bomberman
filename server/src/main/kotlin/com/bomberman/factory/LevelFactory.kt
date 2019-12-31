package com.bomberman.factory

import com.bomberman.TEXTURE_SIZE
import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.Brick
import com.bomberman.entity.mapobject.Wall
import com.bomberman.game.GameState


private const val WIDTH = 15
private const val HEIGHT = 11

class LevelFactory {
  private val level = arrayOf(
      intArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0),
      intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0),
      intArrayOf(0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0),
      intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 2, 1, 0),
      intArrayOf(0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1, 0),
      intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0),
      intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0),
      intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 2, 1, 0, 1, 0),
      intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
      intArrayOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 2, 1, 0, 1, 0),
      intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1))

  fun createLevel(): GameState {
    val gameState = GameState()
    for (y in 0 until HEIGHT) {
      for (x in 0 until WIDTH) {
        val position = Position((x * TEXTURE_SIZE).toFloat(), (y * TEXTURE_SIZE).toFloat())
        when (level[y][x]) {
          1 -> gameState.walls.add(Wall(position))
          2 -> gameState.bricks.add(Brick(position))
        }
      }
    }
    return gameState
  }
}