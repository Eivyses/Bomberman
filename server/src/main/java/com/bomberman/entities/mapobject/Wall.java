package com.bomberman.entities.mapobject;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.Position;

public class Wall extends MapObject {

  public Wall(final Position position) {
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
