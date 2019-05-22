package com.bomberman.entities;

import com.bomberman.constants.MapConst;

public class Wall extends MapObject {

  public Wall(final Position position) {
    super(position);
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
