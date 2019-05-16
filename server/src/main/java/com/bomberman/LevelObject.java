package com.bomberman;

public enum LevelObject {
  TERRAIN(0),
  WALL(1),
  BOMB(2),
  EXPLOSION(3);

  private final int value;

  LevelObject(final int value) {
    this.value = value;
  }

  public static LevelObject fromInteger(final int value) {
    switch (value) {
      case 0:
        return TERRAIN;
      case 1:
        return WALL;
      case 2:
        return BOMB;
      case 3:
        return EXPLOSION;
      default:
        return null;
    }
  }

  public int getValue() {
    return value;
  }
}
