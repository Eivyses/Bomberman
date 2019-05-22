package com.bomberman.game;

import com.bomberman.entities.Bomb;
import com.bomberman.entities.Player;
import com.bomberman.entities.Position;
import com.bomberman.entities.Wall;
import com.bomberman.factories.LevelFactory;
import com.bomberman.factories.PlayerFactory;

import java.time.ZonedDateTime;

public class Game {

  private final PlayerFactory playerFactory;
  private final GameState gameState;
  private final LevelFactory levelFactory;

  public Game() {
    playerFactory = new PlayerFactory();
    levelFactory = new LevelFactory();
    gameState = levelFactory.createLevel();
  }

  public String addPlayer(final String id) {
    final var newPlayer = playerFactory.createPlayer(id, gameState);

    gameState.getPlayers().add(newPlayer);

    return newPlayer.getId();
  }

  public void removePlayer(final String id) {
    gameState.getPlayers().removeIf(x -> x.getId().equals(id));
  }

  public void movePlayer(final String playerId, final Position position) {
    final var player =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    player.ifPresent(value -> value.move(position));
  }

  public void placeBomb(final String playerId) {
    final var currentTime = ZonedDateTime.now();
    final var player = getPlayer(playerId);
    final var bombDurationInSeconds = player.getBombDurationInSeconds();
    final var position = player.getPosition();
    final var explosionTime = currentTime.plusSeconds(bombDurationInSeconds);

    gameState.getBombs().add(new Bomb(position, explosionTime));
  }

  public void initGame() {
    gameState.getWalls().add(new Wall(new Position(32f, 32f)));
  }

  private Player getPlayer(final String playerId) {
    return gameState.getPlayers().stream()
        .filter(x -> x.getId().equals(playerId))
        .findFirst()
        .orElse(null);
  }

  public GameState getGameState() {
    return gameState;
  }
}
