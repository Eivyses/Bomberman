package com.bomberman.game

import com.bomberman.BASE_PLAYER_SPEED
import com.bomberman.PlayerAlreadyExistsException
import com.bomberman.TEXTURE_SIZE
import com.bomberman.TestDataGenerator
import com.bomberman.entity.Direction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GameTest {

  @Test
  fun addPlayer_createsNewPlayer() {
    val game = Game(TestDataGenerator().gameState)
    game.addPlayer("player1")
    assertFalse(game.gameState.players.isEmpty())
    val player = game.gameState.players.first()
    assertEquals("player1", player.id)
  }

  @Test
  fun addPlayer_playerAlreadyExists() {
    val game = Game(TestDataGenerator().gameState)
    game.addPlayer("player1")
    val exception = assertThrows<PlayerAlreadyExistsException> {
      game.addPlayer("player1")
    }
    assertEquals("Player player1 is already connected", exception.message)
  }

  @Test
  fun removePlayer_removesPlayer() {
    val game = Game(TestDataGenerator().gameState)
    game.addPlayer("player1")
    assertFalse(game.gameState.players.isEmpty())
    game.removePlayer("player1")
    assertTrue { game.gameState.players.isEmpty() }
  }

  @Test
  fun placeBomb_willPlaceBomb() {
    val game = Game(TestDataGenerator().gameState)
    game.addPlayer("player1")
    val bomb = game.placeBomb("player1")
    assertNotNull(bomb)
    assertFalse(game.gameState.bombs.isEmpty())
  }

  @Test
  fun placeBomb_wontPlaceTwoBombsAtSamePlace() {
    val game = Game(TestDataGenerator().gameState)
    game.addPlayer("player1")
    game.increaseMaxBombCount("player1")
    val bomb = game.placeBomb("player1")
    assertNotNull(bomb)
    assertFalse(game.gameState.bombs.isEmpty())
    val bomb2 = game.placeBomb("player1")
    assertNull(bomb2)
    assertEquals(1, game.gameState.bombs.size)
  }

  @Test
  fun placeBomb_willPlaceTwoBombs() {
    val game = Game(TestDataGenerator().gameState)
    game.addPlayer("player1")
    game.increaseMaxBombCount("player1")
    game.placeBomb("player1")

    movePlayerByOneTile("player1", game, Direction.UP)
    val bomb = game.placeBomb("player1")
    assertNotNull(bomb)
    assertEquals(2, game.gameState.bombs.size)
  }

  @Test
  fun placeBomb_wontPlaceTwoBombs_noFreeBombs() {
    val game = Game(TestDataGenerator().gameState)

    game.addPlayer("player1")
    game.placeBomb("player1")

    movePlayerByOneTile("player1", game, Direction.UP)
  }

  @ParameterizedTest
  @EnumSource(Direction::class)
  fun movePlayer_canMoveEverywhereFromCenterWithNoObstacles(direction: Direction) {
    val game = Game(TestDataGenerator().createPlayerAtColRow("player1", 5, 6).gameState)
    game.movePlayer("player1", direction)
  }

  private fun movePlayerByOneTile(playerId: String, game: Game, direction: Direction) {
    for (i in 0 until (TEXTURE_SIZE / BASE_PLAYER_SPEED).toInt()) {
      game.movePlayer(playerId, direction)
    }
  }
}