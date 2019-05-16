package com.bomberman.Dto;

import com.bomberman.entities.Position;

public class MoveDto {
  private final Position position;

  public MoveDto(final Position position) {
    this.position = position;
  }

  public MoveDto() {
    this.position = new Position();
  }

  public Position getPosition() {
    return position;
  }
}
