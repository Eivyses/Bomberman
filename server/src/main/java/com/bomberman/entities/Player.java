package com.bomberman.entities;

import com.bomberman.constants.MapConst;
import com.bomberman.game.GameState;
import com.bomberman.utils.Collisions;

public class Player extends MapObject implements Movable {

  private final String id;
  private final int bombDurationInSeconds = 2;
  private final GameState gameState;

  public Player(final String id, final Position position, final GameState gameState) {
    super(position);
    this.id = id;
    this.gameState = gameState;
  }

  public String getId() {
    return id;
  }

  public int getBombDurationInSeconds() {
    return bombDurationInSeconds;
  }

  @Override
  public void move(final Position position) {
    if (Collisions.isOutOfBound(this, position)
        || Collisions.willCollide(this, position, gameState.getObstacles())) {
      return;
    }
    setPosition(position);
  }

  @Override
  public int getTextureWidth() {
    return MapConst.PLAYER_TEXTURE_WIDTH;
  }

  @Override
  public int getTextureHeight() {
    return MapConst.PLAYER_TEXTURE_HEIGHT;
  }
}
