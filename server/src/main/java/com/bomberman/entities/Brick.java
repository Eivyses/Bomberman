package com.bomberman.entities;

import com.bomberman.constants.Configuration;

public class Brick extends MapObject {

  public Brick(final Position position) {
    super(position);
  }

  @Override
  public int getTextureWidth() {
    return Configuration.TEXTURE_SIZE;
  }

  @Override
  public int getTextureHeight() {
    return Configuration.TEXTURE_SIZE;
  }
}
