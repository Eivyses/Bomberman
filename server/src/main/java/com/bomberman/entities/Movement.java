package com.bomberman.entities;

public class Movement {
  private Direction direction;
  private float dt;

  public Movement(final Direction direction, final float dt) {
    this.direction = direction;
    this.dt = dt;
  }

  public Movement(final Direction direction) {
    this.direction = direction;
    this.dt = 0.05f;
  }

  public Movement() {}

  public Direction getDirection() {
    return direction;
  }

  public float getDt() {
    return dt;
  }
}
