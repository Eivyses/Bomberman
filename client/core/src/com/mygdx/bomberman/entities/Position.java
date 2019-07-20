package com.mygdx.bomberman.entities;

public class Position extends JsonSerializable {
  private final float x;
  private final float y;

  public Position(final float x, final float y) {
    this.x = x < 0 ? 0 : x;
    this.y = y < 0 ? 0 : y;
  }

  public Position() {
    this.x = 0f;
    this.y = 0f;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  @Override
  public String toString() {
    return "x=" + x + ", y=" + y;
  }
}
