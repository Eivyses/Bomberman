package com.mygdx.bomberman.entities;

public abstract class MovableObject {
  private Position position;

  public MovableObject() {
    this.position = new Position();
  }

  public MovableObject(final Position position) {
    this.position = position;
  }

  public MovableObject(final float x, final float y) {
    position = new Position(x, y);
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(final Position position) {
    this.position = position;
  }
}
