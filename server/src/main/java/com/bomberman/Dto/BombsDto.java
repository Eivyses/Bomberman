package com.bomberman.Dto;

import java.util.ArrayList;
import java.util.List;

public class BombsDto {
  private List<BombDto> bombs;

  public BombsDto() {
    bombs = new ArrayList<>();
  }

  public List<BombDto> getBombs() {
    return bombs;
  }

  public void setBombs(final List<BombDto> bombs) {
    this.bombs = bombs;
  }
}
