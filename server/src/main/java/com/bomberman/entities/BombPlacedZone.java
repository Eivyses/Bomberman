package com.bomberman.entities;

public class BombPlacedZone {
  private final String playerId;
  private boolean hasLeftBombZone;

  public BombPlacedZone(final String playerId, final boolean hasLeftBombZone) {
    this.playerId = playerId;
    this.hasLeftBombZone = hasLeftBombZone;
  }

  public String getPlayerId() {
    return playerId;
  }

  public boolean isHasLeftBombZone() {
    return hasLeftBombZone;
  }

  public void setHasLeftBombZone() {
    this.hasLeftBombZone = true;
  }

  public boolean hasLeftBombZone() {
    return hasLeftBombZone;
  }
}
