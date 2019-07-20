package com.bomberman.entities.mapobject.pickup;

import com.bomberman.entities.mapobject.movable.Player;

public class BombPickup extends Pickup {

  @Override
  public void apply(final Player player) {
    player.increaseMaxBombCount();
  }
}
