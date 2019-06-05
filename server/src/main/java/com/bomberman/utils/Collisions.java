package com.bomberman.utils;

import com.bomberman.constants.MapConst;
import com.bomberman.entities.MapObject;
import com.bomberman.entities.Position;

import java.util.List;
import java.util.Objects;

public class Collisions {

  public static boolean isOutOfBound(final MapObject mapObject, final Position newPosition) {
    return newPosition.getX() + mapObject.getTextureWidth() > MapConst.MAP_WIDTH
        || newPosition.getY() + mapObject.getTextureHeight() > MapConst.MAP_HEIGHT;
  }

  public static boolean willCollide(
      final MapObject mapObject, final Position newPosition, final List<MapObject> obstacles) {
    return obstacles.stream().anyMatch($obstacle -> collides(mapObject, newPosition, $obstacle));
  }

  private static boolean collides(
      final MapObject movableObject, final Position newPosition, final MapObject obstacle) {

    if (Objects.equals(newPosition, obstacle.getPosition())) {
      return true;
    }

    final Position objectBotLeftPos = newPosition;
    final Position objectTopRightPos =
        new Position(
            newPosition.getX() + movableObject.getTextureWidth(),
            newPosition.getY() + movableObject.getTextureHeight());
    final Position objectTopLefttPos =
        new Position(newPosition.getX(), newPosition.getY() + movableObject.getTextureHeight());
    final Position objectBotRightPos =
        new Position(newPosition.getX() + movableObject.getTextureWidth(), newPosition.getY());

    return collides(objectBotLeftPos, obstacle)
        || collides(objectTopRightPos, obstacle)
        || collides(objectTopLefttPos, obstacle)
        || collides(objectBotRightPos, obstacle);
  }

  private static boolean collides(final Position objectPosition, final MapObject obstacle) {

    return objectPosition.getX() > obstacle.getPosition().getX()
        && objectPosition.getX() < obstacle.getUpperCorner().getX()
        && objectPosition.getY() > obstacle.getPosition().getY()
        && objectPosition.getY() < obstacle.getUpperCorner().getY();
  }
}
