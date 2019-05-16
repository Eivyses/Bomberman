package com.bomberman.Dto;

import com.bomberman.entities.MovableObject;
import com.bomberman.entities.Position;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerDto extends MovableObject {
  private String id;
  private int bombRange;
  private int maxBombCount;

  public PlayerDto(final float x, final float y, final String id) {
    super(x, y);
    this.id = id;
    bombRange = 3;
    maxBombCount = 3;
  }

  public PlayerDto(final Position position, final String id) {
    super(position);
    this.id = id;
  }

  public PlayerDto() {
    super();
  }

  public int getBombRange() {
    return bombRange;
  }

  public boolean canPlaceBomb(final List<BombDto> boms) {
    final List<BombDto> usersBombs =
        boms.stream()
            .filter(b -> b.getPlayer().getId().equals(this.id))
            .collect(Collectors.toList());
    final boolean alreadyOnBomb =
        usersBombs.stream()
            .anyMatch(
                b ->
                    Objects.equals(Position.round(b.getPosition()), Position.round(getPosition())));

    if (alreadyOnBomb) {
      return false;
    }
    return usersBombs.size() < maxBombCount;
  }

  public BombDto placeBomb() {
    return new BombDto(this, getPosition());
  }

  public void setBombRange(final int bombRange) {
    this.bombRange = bombRange;
  }

  public int getMaxBombCount() {
    return maxBombCount;
  }

  public void setMaxBombCount(final int maxBombCount) {
    this.maxBombCount = maxBombCount;
  }

  public String getId() {
    return id;
  }
}
