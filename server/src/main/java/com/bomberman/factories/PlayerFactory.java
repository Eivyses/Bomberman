package com.bomberman.factories;

import com.bomberman.entities.Player;
import com.bomberman.entities.Position;
import com.bomberman.game.GameState;

public class PlayerFactory {

  public Player createPlayer(final String id, final GameState gameState) {
    return new Player(id, new Position(0, 0), gameState);
  }
}
