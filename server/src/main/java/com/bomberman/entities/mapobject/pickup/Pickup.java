package com.bomberman.entities.mapobject.pickup;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.MapObject;
import com.bomberman.entities.mapobject.movable.Player;

public abstract class Pickup extends MapObject {

  public Pickup() {}

  public Pickup(final Position position) {
    super(position);
  }

  public abstract void apply(Player player);

  @Override
  public int getTextureWidth() {
    return Configuration.TEXTURE_SIZE;
  }

  @Override
  public int getTextureHeight() {
    return Configuration.TEXTURE_SIZE;
  }
}
