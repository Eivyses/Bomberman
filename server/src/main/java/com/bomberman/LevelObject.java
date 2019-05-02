package com.bomberman;

public enum LevelObject {
  TERRAIN(0),
  WALL(1),
  BOMB(2);

  private final int value;

  LevelObject(final int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
