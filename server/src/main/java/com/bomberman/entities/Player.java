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
  private final GameState gameState;

  public Player(final String id, final Position position, final GameState gameState) {
    super(position);
    this.id = id;
    this.gameState = gameState;
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
                gameState.getBombs().stream().filter(bomb -> bomb.hasLeftBombZone(this.id)))
            .collect(Collectors.toList());
    final var currentOnBomb =
        gameState.getBombs().stream().filter(bomb -> !bomb.hasLeftBombZone(this.id)).findFirst();

    if (currentOnBomb.isPresent()) {
      final var bomb = currentOnBomb.get();
      if (!Collisions.willCollide(this, position, Collections.singletonList(bomb))) {
        bomb.setHasLeftBombZone(this.id);
      }
    }

    if (Collisions.isOutOfBound(this, position)
        || Collisions.willCollide(this, position, obstacles)) {
      return;
    }

    setPosition(position);
  }

  @Override
  public int getTextureWidth() {
    return MapConst.PLAYER_TEXTURE_WIDTH;
  }

  @Override
  public int getTextureHeight() {
    return MapConst.PLAYER_TEXTURE_HEIGHT;
  }
}
