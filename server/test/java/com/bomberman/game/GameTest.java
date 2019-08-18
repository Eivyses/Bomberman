package com.bomberman.game;

import com.bomberman.TestDataGenerator;
import com.bomberman.constants.Configuration;
import com.bomberman.entities.Direction;
import com.bomberman.entities.Movement;
import com.bomberman.entities.mapobject.Bomb;
import com.bomberman.entities.mapobject.movable.Player;
import com.bomberman.exception.PlayerAlreadyExistsException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class GameTest {

  @Test
  void addPlayer_createsNewPlayer() {
    final Game game = new Game(TestDataGenerator.newGenerator().getGameState());
    game.addPlayer("player1");
    Assertions.assertFalse(game.getGameState().getPlayers().isEmpty());
    final Optional<Player> player = game.getGameState().getPlayers().stream().findFirst();
    Assertions.assertTrue(player.isPresent());
    Assertions.assertEquals("player1", player.get().getId());
  }

  @Test
  void addPlayer_playerAlreadyExists() {
    final Game game = new Game(TestDataGenerator.newGenerator().getGameState());
    game.addPlayer("player1");
    final Exception exception =
        Assertions.assertThrows(
            PlayerAlreadyExistsException.class, () -> game.addPlayer("player1"));

    Assertions.assertEquals("Player player1 is already connected", exception.getMessage());
  }

  @Test
  void removePlayer_removesPlayer() {
    final Game game = new Game(TestDataGenerator.newGenerator().getGameState());
    game.addPlayer("player1");
    Assertions.assertFalse(game.getGameState().getPlayers().isEmpty());
    game.removePlayer("player1");
    Assertions.assertTrue(game.getGameState().getPlayers().isEmpty());
  }

  @Test
  void placeBomb_willPlaceBomb() {
    final Game game = new Game(TestDataGenerator.newGenerator().getGameState());
    game.addPlayer("player1");
    final Optional<Bomb> bomb = game.placeBomb("player1");
    Assertions.assertTrue(bomb.isPresent());
    Assertions.assertFalse(game.getGameState().getBombs().isEmpty());
  }

  @Test
  void placeBomb_wontPlaceTwoBombsAtSamePlace() {
    final Game game = new Game(TestDataGenerator.newGenerator().getGameState());
    game.addPlayer("player1");
    game.increaseMaxBombCount("player1");
    final Optional<Bomb> bomb = game.placeBomb("player1");
    Assertions.assertTrue(bomb.isPresent());
    Assertions.assertFalse(game.getGameState().getBombs().isEmpty());
    final Optional<Bomb> bomb2 = game.placeBomb("player1");
    Assertions.assertFalse(bomb2.isPresent());
    Assertions.assertEquals(1, game.getGameState().getBombs().size());
  }

  @Test
  void placeBomb_willPlaceTwoBomb() {
    final TestDataGenerator generator = TestDataGenerator.newGenerator();
    final Game game = new Game(generator.getGameState());
    game.addPlayer("player1");
    game.increaseMaxBombCount("player1");
    game.placeBomb("player1");

    movePlayerByOneTile("player1", game, Direction.UP);
    final Optional<Bomb> bomb = game.placeBomb("player1");
    Assertions.assertTrue(bomb.isPresent());
    Assertions.assertEquals(2, game.getGameState().getBombs().size());
  }

  @Test
  void placeBomb_wontPlaceTwoBomb_noFreeBombs() {
    final TestDataGenerator generator = TestDataGenerator.newGenerator();
    final Game game = new Game(generator.getGameState());
    game.addPlayer("player1");
    game.placeBomb("player1");

    movePlayerByOneTile("player1", game, Direction.UP);
    final Optional<Bomb> bomb = game.placeBomb("player1");
    Assertions.assertFalse(bomb.isPresent());
    Assertions.assertEquals(1, game.getGameState().getBombs().size());
  }

  @ParameterizedTest
  @EnumSource(Direction.class)
  void movePlayer_canMoveEverywhereFromCenterWithNoObstacles(final Direction direction) {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator().createPlayerAtColRow("player1", 5, 6);
    final Game game = new Game(generator.getGameState());
    game.movePlayer("player1", new Movement(direction));
  }

  private void movePlayerByOneTile(
      final String playerId, final Game game, final Direction direction) {
    for (int i = 0; i < Configuration.TEXTURE_SIZE / Configuration.BASE_PLAYER_SPEED; i++) {
      game.movePlayer(playerId, new Movement(direction));
    }
  }
}
