package com.bomberman.utils;

import com.bomberman.constants.MapConst;
import com.bomberman.entities.MapObject;
import com.bomberman.entities.Position;
import com.bomberman.game.GameState;
import java.util.List;
import java.util.Objects;

public class Collisions {

  // TODO: refactor this fiesta
  public static boolean isInExplosionRange(
      final int range,
      final MapObject fromObject,
      final MapObject toObject,
      final GameState gameState) {
    for (int x = 1; x <= range; x++) {
      final var position =
          new Position(
              fromObject.getPosition().getX() + (x * fromObject.getTextureWidth()),
              fromObject.getPosition().getY());
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
    for (int x = 1; x <= range; x++) {
      final var position =
          new Position(
              fromObject.getPosition().getX() - (x * fromObject.getTextureWidth()),
              fromObject.getPosition().getY());

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

    for (int y = 1; y <= range; y++) {
      final var position =
          new Position(
              fromObject.getPosition().getX(),
              fromObject.getPosition().getY() + (y * fromObject.getTextureHeight()));

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
    for (int y = 1; y <= range; y++) {

      final var position =
          new Position(
              fromObject.getPosition().getX(),
              fromObject.getPosition().getY() - (y * fromObject.getTextureHeight()));

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
