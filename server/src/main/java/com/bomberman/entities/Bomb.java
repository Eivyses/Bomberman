package com.bomberman.entities;

import com.bomberman.constants.MapConst;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bomb extends MapObject {

  private final ZonedDateTime explosionTime;
  private final List<BombPlacedZone> bombPlacedZoneList;

  public Bomb(
      final Position position, final ZonedDateTime explosionTime, final List<Player> players) {
    super(position);
    this.explosionTime = explosionTime;
    bombPlacedZoneList = new ArrayList<>();
    players.forEach(p -> bombPlacedZoneList.add(new BombPlacedZone(p.getId(), false)));
  }

  public void setHasLeftBombZone(final String playerId) {
    final var player =
        bombPlacedZoneList.stream().filter(bpz -> bpz.getPlayerId().equals(playerId)).findFirst();
    player.ifPresent(BombPlacedZone::setHasLeftBombZone);
  }

  @Override
  public int getTextureWidth() {
    return MapConst.TEXTURE_SIZE;
  }

  @Override
  public int getTextureHeight() {
    return MapConst.TEXTURE_SIZE;
  }

  public boolean hasLeftBombZone(final String playerId) {
    final var player =
        bombPlacedZoneList.stream().filter(bpz -> bpz.getPlayerId().equals(playerId)).findFirst();
    return player.map(BombPlacedZone::hasLeftBombZone).orElse(true);
  }
}
