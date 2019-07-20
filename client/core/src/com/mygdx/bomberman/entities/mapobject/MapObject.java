package com.mygdx.bomberman.entities.mapobject;

import com.mygdx.bomberman.entities.Position;

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

  @Override
  public String toString() {
    return "MapObject " + "position= " + position.toString();
  }
}
