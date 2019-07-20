package com.mygdx.bomberman.entities.mapobject;

import com.mygdx.bomberman.entities.Position;

public class Player extends MapObject {

  private final String id;
  private final boolean dead;
  private final int killCount;
  private final float stateTime;

  public Player(
      final String id,
      final Position position,
      final boolean dead,
      final int killCount,
      final float stateTime) {
    super(position);
    this.id = id;
    this.dead = dead;
    this.killCount = killCount;
    this.stateTime = stateTime;
  }

  public String getId() {
    return id;
  }

  public boolean isDead() {
    return dead;
  }

  public float getStateTime() {
    return stateTime;
  }

  public int getKillCount() {
    return killCount;
  }
}
