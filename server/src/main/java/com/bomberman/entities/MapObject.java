package com.bomberman.entities;

public abstract class MapObject {
  private Position position;

  public MapObject() {
    this.position = new Position();
  }

  public MapObject(final Position position) {
    this.position = position;
  }

  public MapObject(final float x, final float y) {
    position = new Position(x, y);
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(final Position position) {
    this.position = position;
  }

  public Position getTopLeft() {
    return new Position(position.getX(), position.getY() + getTextureHeight());
  }

  public Position getBotRight() {
    return new Position(position.getX() + getTextureWidth(), position.getY());
  }

  public abstract int getTextureWidth();

  public abstract int getTextureHeight();
}
