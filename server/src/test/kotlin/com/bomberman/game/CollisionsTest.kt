package com.bomberman.game

import com.bomberman.TestDataGenerator
import com.bomberman.positionAtColRow
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollisionsTest {

  @Test
  fun willCollide_playerAboveObstacle_moveDown() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 6)

    val player = generator.getPlayer("player1")

    assertTrue { player.willCollide(positionAtColRow(7, 6), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerAboveObstacle_moveUp() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 6)

    val player = generator.getPlayer("player1")

    assertFalse { player.willCollide(positionAtColRow(7, 4), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerAboveObstacle_moveLeft() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 6)

    val player = generator.getPlayer("player1")

    assertFalse { player.willCollide(positionAtColRow(6, 5), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerAboveObstacle_moveRight() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 6)

    val player = generator.getPlayer("player1")

    assertFalse { player.willCollide(positionAtColRow(8, 5), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerBelowObstacle_moveDown() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 4)

    val player = generator.getPlayer("player1")

    assertFalse { player.willCollide(positionAtColRow(7, 6), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerBelowObstacle_moveUp() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 4)

    val player = generator.getPlayer("player1")

    assertTrue { player.willCollide(positionAtColRow(7, 4), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerBelowObstacle_moveLeft() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 4)

    val player = generator.getPlayer("player1")

    assertFalse { player.willCollide(positionAtColRow(6, 5), generator.gameState.obstacles) }
  }

  @Test
  fun willCollide_playerBelowObstacle_moveRight() {
    val generator = TestDataGenerator()
        .createPlayerAtColRow("player1", 7, 5)
        .createWallAtColRow(7, 4)

    val player = generator.getPlayer("player1")

    assertFalse { player.willCollide(positionAtColRow(8, 5), generator.gameState.obstacles) }
  }

}