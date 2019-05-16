package com.bomberman.Dto;

import com.bomberman.entities.MovableObject;
import com.bomberman.entities.Position;

public class BombDto extends MovableObject {
  private int bombRange;
  private PlayerDto player;

  public BombDto() {
    super();
  }

  public BombDto(final PlayerDto player, final Position position) {
    super(Position.round(position));
    this.bombRange = player.getBombRange();
    this.player = player;
  }

  public BombDto(final PlayerDto player, final int x, final int y) {
    this(player, new Position(x, y));
  }

  public int getBombRange() {
    return bombRange;
  }

  public PlayerDto getPlayer() {
    return player;
  }
}
