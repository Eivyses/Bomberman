package com.bomberman.factories;

import com.bomberman.entities.mapobject.Brick;
import com.bomberman.entities.mapobject.pickup.BombPickup;

public class PickupFactory {

  public BombPickup createBombPickup(final Brick brick) {
    return new BombPickup(brick.getPosition());
  }
}
