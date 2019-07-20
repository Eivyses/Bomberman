package com.bomberman.entities;

import com.bomberman.constants.Configuration;
import com.bomberman.game.GameState;
import com.bomberman.utils.Collisions;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player extends MapObject implements Movable {

  private final String id;
  private final int bombDurationInSeconds;
  private final float speed;
  private final int bombRange;
  private final GameState gameState;
  private boolean dead;
  private int killCount;
  private float stateTime;

  public Player(final String id, final Position position, final GameState gameState) {
    super(position);
    this.id = id;
    this.gameState = gameState;
    speed = Configuration.BASE_PLAYER_SPEED;
    bombRange = Configuration.BASE_BOMB_RANGE;
    bombDurationInSeconds = 3;
    dead = false;
    killCount = 0;
    stateTime = 0f;
  }

  public String getId() {
    return id;
  }

  public float getSpeed() {
    return speed;
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead() {
    dead = true;
  }

  public float getStateTime() {
    return stateTime;
  }

  public void addStateTime(final float stateTime) {
    this.stateTime += stateTime;
  }

  public void respawn() {
    setPosition(new Position(0, 0));
    dead = false;
    System.out.println(String.format("Player %s resurrected", id));
  }

  public int getKillCount() {
    return killCount;
  }

  public void killedEnemy() {
    killCount++;
  }

  public void killedHimself() {
    killCount--;
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

  int getBombRange() {
    return bombRange;
  }

  @Override
  public int getTextureWidth() {
    return Configuration.PLAYER_TEXTURE_WIDTH;
  }

  @Override
  public int getTextureHeight() {
    return Configuration.PLAYER_TEXTURE_HEIGHT;
  }

  @Override
  public String toString() {
    return String.format("PlayerId: %s, position: %s", id, getPosition().toString());
  }
}
