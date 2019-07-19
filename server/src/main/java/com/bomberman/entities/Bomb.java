package com.bomberman.entities;

import com.bomberman.constants.MapConst;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bomb extends MapObject {

  private final String bombId;
  private final int explosionTime;
  private final String playerId;
  private final int bombRange;
  private final List<BombPlacedZone> bombPlacedZoneList;

  public Bomb(
      final Position position, final int explosionTime, final Player player, final List<Player> players) {
    super(position);
    bombId = UUID.randomUUID().toString();
    this.explosionTime = explosionTime;
    playerId = player.getId();
    bombRange = player.getBombRange();
    bombPlacedZoneList = new ArrayList<>();
    players.forEach(p -> bombPlacedZoneList.add(new BombPlacedZone(p.getId(), false)));
  }

  public void setHasLeftBombZone(final String playerId) {
    final var player =
        bombPlacedZoneList.stream().filter(bpz -> bpz.getPlayerId().equals(playerId)).findFirst();
    player.ifPresent(BombPlacedZone::setHasLeftBombZone);
  }

  public String getBombId() {
    return bombId;
  }

  public int getBombRange() {
    return bombRange;
  }

  public String getPlayerId() {
    return playerId;
  }

  public int getExplosionTime() {
    return explosionTime;
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

  @Override
  public String toString() {
    return String.format("BOMB, position: %s, range: %d", getPosition().toString(), bombRange);
  }
}
