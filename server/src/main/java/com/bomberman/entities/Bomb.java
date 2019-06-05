package com.bomberman.entities;

import com.bomberman.constants.MapConst;

import java.time.ZonedDateTime;

public class Bomb extends MapObject {

  private final ZonedDateTime explosionTime;
  private final String playerId;
  private boolean hasLeftBombZone;

  public Bomb(final Position position, final ZonedDateTime explosionTime, final String playerId) {
    super(position);
    this.playerId = playerId;
    this.explosionTime = explosionTime;
    this.hasLeftBombZone = false;
  }

  public void setHasLeftBombZone() {
    this.hasLeftBombZone = true;
  }

  public String getPlayerId() {
    return playerId;
  }

  @Override
  public int getTextureWidth() {
    return MapConst.TEXTURE_SIZE;
  }

  @Override
  public int getTextureHeight() {
    return MapConst.TEXTURE_SIZE;
  }

  public boolean hasLeftBombZone(String playerId) {
    if (playerId.equals(this.playerId)) {
      return hasLeftBombZone;
    } else return true;
  }
}
