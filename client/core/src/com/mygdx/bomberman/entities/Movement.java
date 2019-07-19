package com.mygdx.bomberman.entities;

public class Movement extends JsonSerializable {
  private final Direction direction;
  private final float dt;

  public Movement(final Direction direction, final float dt) {
    this.direction = direction;
    this.dt = dt;
  }

  public Direction getDirection() {
    return direction;
  }

  public float getDt() {
    return dt;
  }
}
