package com.bomberman.utils;

import com.bomberman.constants.MapConst;
import com.bomberman.entities.MapObject;
import com.bomberman.entities.Position;

import java.util.List;

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

    final Position playerBotLeftPos = newPosition;
    final Position playerTopRightPos =
        new Position(
            newPosition.getX() + movableObject.getTextureWidth(),
            newPosition.getY() + movableObject.getTextureHeight());
    final Position playerTopLefttPos =
        new Position(newPosition.getX(), newPosition.getY() + movableObject.getTextureHeight());
    final Position playerBotRightPos =
        new Position(newPosition.getX() + movableObject.getTextureWidth(), newPosition.getY());

    return collides(playerBotLeftPos, obstacle)
        || collides(playerTopRightPos, obstacle)
        || collides(playerTopLefttPos, obstacle)
        || collides(playerBotRightPos, obstacle);
  }

  private static boolean collides(final Position playerPosition, final MapObject obstacle) {

    return playerPosition.getX() >= obstacle.getPosition().getX()
        && playerPosition.getX() <= obstacle.getUpperCorner().getX()
        && playerPosition.getY() >= obstacle.getPosition().getY()
        && playerPosition.getY() <= obstacle.getUpperCorner().getY();
  }
}
