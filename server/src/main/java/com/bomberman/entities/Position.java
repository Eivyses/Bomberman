package com.bomberman.entities;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.mapobject.MapObject;
import java.util.Objects;

public class Position {
  private final float x;
  private final float y;

  public Position(final float x, final float y) {
    this.x = x < 0 ? 0 : x;
    this.y = y < 0 ? 0 : y;
  }

  public static Position round(final Position pos) {
    final int xPos = ((int) (pos.x / Configuration.TEXTURE_SIZE)) * Configuration.TEXTURE_SIZE;
    final int yPos = ((int) (pos.y / Configuration.TEXTURE_SIZE)) * Configuration.TEXTURE_SIZE;
    return new Position(xPos, yPos);
  }

  public Position getTopLeft(final MapObject mapObject) {
    return new Position(x, y + mapObject.getTextureHeight() - 0.01f);
  }

  public Position getBotRight(final MapObject mapObject) {
    return new Position(x + mapObject.getTextureWidth() - 0.01f, y);
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
