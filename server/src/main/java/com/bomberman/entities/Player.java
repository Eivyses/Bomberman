package com.bomberman.entities;

import com.bomberman.constants.MapConst;
import com.bomberman.game.GameState;
import com.bomberman.utils.Collisions;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player extends MapObject implements Movable {

  private final String id;
  private final int bombDurationInSeconds = 3;
  private final float speed;
  private final int bombRagne;
  private final GameState gameState;

  public Player(final String id, final Position position, final GameState gameState) {
    super(position);
    this.id = id;
    this.gameState = gameState;
    speed = 120f;
    bombRagne = 2;
  }

  public String getId() {
    return id;
  }

  public int getBombDurationInSeconds() {
    return bombDurationInSeconds;
  }

  @Override
  public void move(final Position position) {
    final var obstacles =
        Stream.concat(
                gameState.getWalls().stream(),
                gameState.getBombs().stream().filter(bomb -> bomb.hasLeftBombZone(id)))
            .collect(Collectors.toList());
    final var currentOnBomb =
        gameState.getBombs().stream().filter(bomb -> !bomb.hasLeftBombZone(id)).findFirst();

    currentOnBomb
        .filter($bomb -> willNotCollide(position, $bomb))
        .ifPresent($bomb -> $bomb.setHasLeftBombZone(id));

    if (Collisions.isOutOfBound(this, position)
        || Collisions.willCollide(this, position, obstacles)) {
      return;
    }

    setPosition(position);
  }

  private boolean willNotCollide(final Position position, final Bomb bomb) {
    return !Collisions.willCollide(this, position, Collections.singletonList(bomb));
  }

  public int getBombRagne() {
    return bombRagne;
  }

  @Override
  public int getTextureWidth() {
    return MapConst.PLAYER_TEXTURE_WIDTH;
  }

  @Override
  public int getTextureHeight() {
    return MapConst.PLAYER_TEXTURE_HEIGHT;
  }

  @Override
  public String toString() {
    return String.format("PlayerId: %s, position: %s", id, getPosition().toString());
  }
}
