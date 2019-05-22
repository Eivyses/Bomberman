package com.bomberman.entities;

import com.bomberman.constants.MapConst;

import java.util.Objects;

public class Position {
  private final float x;
  private final float y;

  public Position(final float x, final float y) {
    this.x = x < 0 ? 0 : x;
    this.y = y < 0 ? 0 : y;
  }

  public static Position round(final Position pos) {
    final int xPos = ((int) (pos.x / MapConst.TEXTURE_SIZE)) * MapConst.TEXTURE_SIZE;
    final int yPos = ((int) (pos.y / MapConst.TEXTURE_SIZE)) * MapConst.TEXTURE_SIZE;
    return new Position(xPos, yPos);
  }

  public Position() {
    this.x = 0f;
    this.y = 0f;
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Position)) {
      return false;
    }
    if (o == this) {
      return true;
    }
    final Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return String.format("x: %f.2, y: %f.2", x, y);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }
}
