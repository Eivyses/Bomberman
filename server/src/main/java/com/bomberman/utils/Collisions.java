package com.bomberman.utils;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.MapObject;
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
      final boolean isDirectionDown,
      final boolean xAxis) {
    for (int i = 1; i <= range; i++) {
      final float xPos;
      final float yPos;
      if (xAxis) {
        yPos = fromObject.getPosition().getY();
        if (isDirectionDown) {
          xPos = fromObject.getPosition().getX() - (i * fromObject.getTextureWidth());
        } else {
          xPos = fromObject.getPosition().getX() + (i * fromObject.getTextureWidth());
        }
      } else {
        xPos = fromObject.getPosition().getX();
        if (isDirectionDown) {
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
    return newPosition.getX() + mapObject.getTextureWidth() > Configuration.MAP_WIDTH
        || newPosition.getY() + mapObject.getTextureHeight() > Configuration.MAP_HEIGHT;
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

    final Position l1 = newPosition.getTopLeft(movableObject);
    final Position r1 = newPosition.getBotRight(movableObject);
    final Position l2 = obstacle.getTopLeft();
    final Position r2 = obstacle.getBotRight();

    // If one rectangle is on left side of other
    if (l1.getX() > r2.getX() || l2.getX() > r1.getX()) {
      return false;
    }

    // If one rectangle is above other
    if (l1.getY() < r2.getY() || l2.getY() < r1.getY()) {
      return false;
    }

    return true;
  }

  public static boolean isAtSameSquare(
      final Position firstPosition, final Position secondPosition) {
    return Objects.equals(firstPosition, secondPosition);
  }
}
