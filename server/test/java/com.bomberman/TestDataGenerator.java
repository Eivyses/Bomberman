package com.bomberman;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.Wall;
import com.bomberman.entities.mapobject.movable.Player;
import com.bomberman.exception.PlayerNotFoundException;
import com.bomberman.game.GameState;
import java.security.InvalidParameterException;

public class TestDataGenerator {
  private final GameState gameState;

  private TestDataGenerator() {
    gameState = new GameState();
  }

  public GameState getGameState() {
    return gameState;
  }

  public Player getPlayer(final String playerId) {
    return gameState.getPlayers().stream()
        .filter(player -> player.getId().equals(playerId))
        .findFirst()
        .orElseThrow(PlayerNotFoundException::new);
  }

  public static Position positionAtColRow(final int col, final int row) {
    return new Position(col * Configuration.TEXTURE_SIZE, row * Configuration.TEXTURE_SIZE);
  }

  public static TestDataGenerator newGenerator() {
    return new TestDataGenerator();
  }

  public TestDataGenerator createPlayerAtColRow(
      final String playerId, final int col, final int row) {
    return createPlayerAtPosition(
        playerId, col * Configuration.TEXTURE_SIZE, row * Configuration.TEXTURE_SIZE);
  }

  public TestDataGenerator createPlayerAtPosition(
      final String playerId, final float x, final float y) {
    checkBoundaries(x, y);
    gameState.getPlayers().add(new Player(playerId, new Position(x, y), gameState));
    return this;
  }

  public TestDataGenerator createWallAtColRow(final int col, final int row) {
    return createWallAtPosition(col * Configuration.TEXTURE_SIZE, row * Configuration.TEXTURE_SIZE);
  }

  public TestDataGenerator createWallAtPosition(final float x, final float y) {
    checkBoundaries(x, y);
    gameState.getWalls().add(new Wall(new Position(x, y)));
    return this;
  }

  private void checkBoundaries(final float x, final float y) {
    if (x > Configuration.MAP_WIDTH) {
      throw new InvalidParameterException(
          String.format("Position X can't be more than: %d", Configuration.MAP_WIDTH));
    }
    if (y > Configuration.MAP_HEIGHT) {
      throw new InvalidParameterException(
          String.format("Position Y can't be more than: %d", Configuration.MAP_HEIGHT));
    }
  }
}
