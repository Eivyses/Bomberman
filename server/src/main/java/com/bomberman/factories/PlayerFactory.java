package com.bomberman.factories;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.movable.Player;
import com.bomberman.game.GameState;

public class PlayerFactory {

  public Player createPlayer(final String id, final GameState gameState) {
    return new Player(
        id,
        new Position(0, Configuration.MAP_HEIGHT - Configuration.PLAYER_TEXTURE_HEIGHT),
        gameState);
  }
}
