package com.mygdx.bomberman.entities;

public class Player extends MapObject {

  private final String id;
  private final int bombDurationInSeconds = 2;

  public Player(final String id, final Position position) {
    super(position);
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public int getBombDurationInSeconds() {
    return bombDurationInSeconds;
  }
}
