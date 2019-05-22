package com.mygdx.bomberman.entities;

import java.time.ZonedDateTime;

public class Bomb extends MapObject {

  private final ZonedDateTime explosionTime;

  public Bomb(final Position position, final ZonedDateTime explosionTime) {
    super(position);
    this.explosionTime = explosionTime;
  }
}
