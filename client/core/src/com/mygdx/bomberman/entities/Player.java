package com.mygdx.bomberman.entities;

public class Player extends MapObject {

  private final String id;
  private final boolean dead;
  private final int killCount;

  public Player(final String id, final Position position, final boolean dead, final int killCount) {
    super(position);
    this.id = id;
    this.dead = dead;
    this.killCount = killCount;
  }

  public String getId() {
    return id;
  }

  public boolean isDead() {
    return dead;
  }

  public int getKillCount() {
    return killCount;
  }
}
