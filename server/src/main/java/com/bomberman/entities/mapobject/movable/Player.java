package com.bomberman.entities.mapobject.movable;

import com.bomberman.constants.Configuration;
import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.Bomb;
import com.bomberman.entities.mapobject.MapObject;
import com.bomberman.game.GameState;
import com.bomberman.utils.Collisions;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Player extends MapObject implements Movable {

  private final String id;
  private final int bombDurationInSeconds;
  private float speed;
  private int bombRange;
  private int placedBombCount;
  private int maxBombCount;
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
    bombDurationInSeconds = Configuration.BOMB_DURATION;
    dead = false;
    killCount = 0;
    placedBombCount = 0;
    maxBombCount = 1;
    stateTime = 0f;
  }

  public void increasePlayerSpeed() {
    if (speed >= Configuration.MAX_PLAYER_SPEED) {
      return;
    }
    speed += Configuration.PLAYER_SPEED_INCREASE_VALUE;
    System.out.println("player speed increased to " + speed);
  }

  public void decreasePlayerSpeed() {
    if (speed <= 1f) {
      return;
    }
    speed -= Configuration.PLAYER_SPEED_INCREASE_VALUE;
    System.out.println("player speed decreased to " + speed);
  }

  public void increaseBombRange() {
    bombRange += 1;
    System.out.println("bomb range increased to " + bombRange);
  }

  public void decreaseBombRange() {
    if (bombRange <= 0) {
      return;
    }
    bombRange -= 1;
    System.out.println("bomb range decreased to " + bombRange);
  }

  public void increaseMaxBombCount() {
    maxBombCount++;
    System.out.println("max bomb count increased to " + bombRange);
  }

  public void incrementPlacedBombCount() {
    placedBombCount++;
  }

  public void decrementPlacedBombCount() {
    placedBombCount--;
  }

  public boolean canPlaceBomb() {
    return placedBombCount < maxBombCount;
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
                Stream.concat(gameState.getWalls().stream(), gameState.getBricks().stream()),
                gameState.getBombs().stream().filter($bomb -> $bomb.hasLeftBombZone(id)))
            .collect(Collectors.toList());
    final var currentOnBomb =
        gameState.getBombs().stream().filter($bomb -> !$bomb.hasLeftBombZone(id)).findFirst();

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

  public int getBombRange() {
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
