package com.bomberman.Dto;

import com.bomberman.LevelObject;
import com.bomberman.utils.Rectangle;

public class LevelDto {

  private int width;
  private int height;
  private int[][] level;

  public LevelDto() {
    width = 15;
    height = 11;
    initLevel();
  }

  private void initLevel() {
    level = // 15x11
        new int[][] {
          {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
          {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
          {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
          {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
          {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
          {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}
        };
  }

  public int[][] getLevel() {
    return level;
  }

  public void setLevel(int[][] level) {
    this.level = level;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setValue(int x, int y, int val) {
    level[y][x] = val;
  }

  public boolean validMove(MoveDto moveDto, PlayerDto playerDto) {
    Rectangle moveRectangle = new Rectangle(moveDto, height);
    Rectangle playerRectangle = new Rectangle(playerDto, height);

    if (moveRectangle.collidesWith(level, LevelObject.WALL)) {
      return false;
    }

    return moveRectangle.triesToLeaveObject(level, LevelObject.BOMB, playerRectangle);
  }

  public void placeBomb(float x, float y) {
    int gridX = formatPositionX(x);
    int gridY = formatPositionY(y);
    setValue(gridX, gridY, LevelObject.BOMB.getValue());
  }

  public void removeBomb(float x, float y) {
    int gridX = formatPositionX(x);
    int gridY = formatPositionY(y);
    setValue(gridX, gridY, LevelObject.TERRAIN.getValue());
  }

  public boolean canPlaceBomb(float x, float y) {
    int gridX = formatPositionX(x);
    int gridY = formatPositionY(y);
    return level[gridY][gridX] != LevelObject.BOMB.getValue();
  }

  private int formatPositionX(float pos) {
    int roundPos = (int) pos;
    int formatted = roundPos / 32;
    return formatted;
  }

  private int formatPositionY(float pos) {
    int roundPos = (int) pos;
    int formatted = roundPos / 32;
    return height - 1 - formatted;
  }
}
