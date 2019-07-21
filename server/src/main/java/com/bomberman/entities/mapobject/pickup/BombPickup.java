package com.bomberman.entities.mapobject.pickup;

import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.movable.Player;

public class BombPickup extends Pickup {

  public BombPickup(final Position position) {
    super(position);
  }

  @Override
  public void apply(final Player player) {
    player.increaseMaxBombCount();
  }

  @Override
  public String getClassName() {
    return BombPickup.class.getSimpleName();
  }
}
