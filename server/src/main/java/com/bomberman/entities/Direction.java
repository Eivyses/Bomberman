package com.bomberman.entities;

public enum Direction {
  UP(0),
  DOWN(1),
  LEFT(2),
  RIGHT(3);

  private final int value;

  Direction(final int value) {
    this.value = value;
  }

  public Position buildPosition(final Position playerPosition, final float movementSpeed) {
    final float x;
    final float y;

    switch (this) {
      case UP:
        x = playerPosition.getX();
        y = playerPosition.getY() - movementSpeed;
        break;
      case DOWN:
        x = playerPosition.getX();
        y = playerPosition.getY() + movementSpeed;
        break;
      case LEFT:
        x = playerPosition.getX() - movementSpeed;
        y = playerPosition.getY();
        break;
      case RIGHT:
        x = playerPosition.getX() + movementSpeed;
        y = playerPosition.getY();
        break;
      default:
        x = playerPosition.getX();
        y = playerPosition.getY();
    }
    return new Position(x, y);
  }

  public int getValue() {
    return value;
  }
}
