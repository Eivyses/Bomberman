package com.bomberman.entities;

import com.bomberman.constants.Configuration;
import com.bomberman.game.GameState;
import com.bomberman.utils.Collisions;
import java.util.ArrayList;
import java.util.List;

public class BombExplosion extends MapObject {

  private final String bombId;
  private final String playerId;

  private BombExplosion(final Position position, final String bombId, final String playerId) {
    super(position);
    this.bombId = bombId;
    this.playerId = playerId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getBombId() {
    return bombId;
  }

  @Override
  public int getTextureWidth() {
    return Configuration.TEXTURE_SIZE;
  }

  @Override
  public int getTextureHeight() {
    return Configuration.TEXTURE_SIZE;
  }

  public static List<BombExplosion> of(
      final Bomb bomb, final String bombId, final String playerId, final GameState gameState) {
    final List<BombExplosion> bombExplosions = new ArrayList<>();
    bombExplosions.add(new BombExplosion(bomb.getPosition(), bombId, playerId));

    for (int x = 1; x <= bomb.getBombRange(); x++) {
      final var position =
          new Position(
              bomb.getPosition().getX() + (x * bomb.getTextureWidth()), bomb.getPosition().getY());
      final boolean willHitWall =
          gameState.getWalls().stream()
              .anyMatch(wall -> Collisions.isAtSameSquare(wall.getPosition(), position));
      if (willHitWall) {
        break;
      }
      bombExplosions.add(new BombExplosion(position, bombId, playerId));
    }

    for (int x = 1; x <= bomb.getBombRange(); x++) {
      final var position =
          new Position(
              bomb.getPosition().getX() - (x * bomb.getTextureWidth()), bomb.getPosition().getY());
      final boolean willHitWall =
          gameState.getWalls().stream()
              .anyMatch(wall -> Collisions.isAtSameSquare(wall.getPosition(), position));
      if (willHitWall) {
        break;
      }
      bombExplosions.add(new BombExplosion(position, bombId, playerId));
    }

    for (int y = 1; y <= bomb.getBombRange(); y++) {
      final var position =
          new Position(
              bomb.getPosition().getX(), bomb.getPosition().getY() + (y * bomb.getTextureHeight()));
      final boolean willHitWall =
          gameState.getWalls().stream()
              .anyMatch(wall -> Collisions.isAtSameSquare(wall.getPosition(), position));
      if (willHitWall) {
        break;
      }
      bombExplosions.add(new BombExplosion(position, bombId, playerId));
    }

    for (int y = 1; y <= bomb.getBombRange(); y++) {
      final var position =
          new Position(
              bomb.getPosition().getX(), bomb.getPosition().getY() - (y * bomb.getTextureHeight()));
      final boolean willHitWall =
          gameState.getWalls().stream()
              .anyMatch(wall -> Collisions.isAtSameSquare(wall.getPosition(), position));
      if (willHitWall) {
        break;
      }
      bombExplosions.add(new BombExplosion(position, bombId, playerId));
    }
    return bombExplosions;
  }
}
