package com.mygdx.bomberman.game;

import com.mygdx.bomberman.entities.mapobject.Player;

public class Game {
  private GameState gameState;
  private String playerId;

  public Game() {
    gameState = new GameState();
  }

  public GameState getGameState() {
    return gameState;
  }

  public void setGameState(final GameState gameState) {
    this.gameState = gameState;
  }

  public void setPlayerId(final String playerId) {
    this.playerId = playerId;
  }

  public Player getCurrentPlayer() {
    return gameState.getPlayers().stream()
        .filter(x -> x.getId().equals(playerId))
        .findFirst()
        .orElse(null);
  }
}
