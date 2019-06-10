package com.bomberman.utils;

import com.bomberman.constants.MapConst;
import com.bomberman.entities.MapObject;
import com.bomberman.entities.Position;
import com.bomberman.game.GameState;
import java.util.List;
import java.util.Objects;

public class Collisions {

  public static boolean isInExplosionRange(
      final int range,
      final MapObject fromObject,
      final MapObject toObject,
      final GameState gameState) {

    if (isInExplosionRange(range, fromObject, toObject, gameState, true, true)) {
      return true;
    }
    if (isInExplosionRange(range, fromObject, toObject, gameState, false, true)) {
      return true;
    }
    if (isInExplosionRange(range, fromObject, toObject, gameState, true, false)) {
      return true;
    }
    return isInExplosionRange(range, fromObject, toObject, gameState, false, false);
  }

  private static boolean isInExplosionRange(
      final int range,
      final MapObject fromObject,
      final MapObject toObject,
      final GameState gameState,
      final boolean lower,
      final boolean xAxis) {
    for (int i = 1; i <= range; i++) {
      final float xPos;
      final float yPos;
      if (xAxis) {
        yPos = fromObject.getPosition().getY();
        if (lower) {
          xPos = fromObject.getPosition().getX() - (i * fromObject.getTextureWidth());
        } else {
          xPos = fromObject.getPosition().getX() + (i * fromObject.getTextureWidth());
        }
      } else {
        xPos = fromObject.getPosition().getX();
        if (lower) {
          yPos = fromObject.getPosition().getY() - (i * fromObject.getTextureHeight());
        } else {
          yPos = fromObject.getPosition().getY() + (i * fromObject.getTextureHeight());
        }
      }

      final var position = new Position(xPos, yPos);

      final boolean willHitWall =
          gameState.getWalls().stream()
              .anyMatch(wall -> Collisions.isAtSameSquare(wall.getPosition(), position));
      if (willHitWall) {
        break;
      }

      if (isAtSameSquare(position, toObject.getPosition())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isOutOfBound(final MapObject mapObject, final Position newPosition) {
    return newPosition.getX() + mapObject.getTextureWidth() > MapConst.MAP_WIDTH
        || newPosition.getY() + mapObject.getTextureHeight() > MapConst.MAP_HEIGHT;
  }

  public static boolean willCollide(
      final MapObject mapObject, final Position newPosition, final List<MapObject> obstacles) {
    return obstacles.stream().anyMatch($obstacle -> willCollide(mapObject, newPosition, $obstacle));
  }

  public static boolean willCollide(
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

    return willCollide(objectBotLeftPos, obstacle)
        || willCollide(objectTopRightPos, obstacle)
        || willCollide(objectTopLefttPos, obstacle)
        || willCollide(objectBotRightPos, obstacle);
  }

  private static boolean willCollide(final Position objectPosition, final MapObject obstacle) {
    return objectPosition.getX() > obstacle.getPosition().getX()
        && objectPosition.getX() < obstacle.getUpperCorner().getX()
        && objectPosition.getY() > obstacle.getPosition().getY()
        && objectPosition.getY() < obstacle.getUpperCorner().getY();
  }

  public static boolean isAtSameSquare(
      final Position firstPosition, final Position secondPosition) {
    return Objects.equals(firstPosition, secondPosition);
  }
}
