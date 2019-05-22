package com.bomberman.entities;

import com.bomberman.constants.MapConst;

import java.time.ZonedDateTime;

public class Bomb extends MapObject {

  private final ZonedDateTime explosionTime;

  public Bomb(final Position position, final ZonedDateTime explosionTime) {
    super(position);
    this.explosionTime = explosionTime;
  }

  @Override
  public int getTextureWidth() {
    return MapConst.TEXTURE_SIZE;
  }

  @Override
  public int getTextureHeight() {
    return MapConst.TEXTURE_SIZE;
  }
}
