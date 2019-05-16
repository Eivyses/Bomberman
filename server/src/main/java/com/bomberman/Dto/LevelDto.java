package com.bomberman.Dto;

import com.bomberman.LevelObject;
import com.bomberman.constants.MapConst;
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

  public void setLevel(final int[][] level) {
    this.level = level;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public void setValue(final int x, final int y, final int val) {
    level[y][x] = val;
  }

  public void createBombAction(final BombDto bomb, final LevelObject levelObject) {
    final int bombRange = bomb.getBombRange();
    final int xPos = formatPositionX(bomb.getPosition().getX());
    final int yPos = formatPositionY(bomb.getPosition().getY());

    setLevelField(xPos, yPos, levelObject);
    for (int x = xPos - 1; x > xPos - bombRange && x >= 0; x--) {
      if (!checkAndCreateBombAction(x, yPos, levelObject)) {
        break;
      }
    }

    for (int x = xPos + 1; x < xPos + bombRange && x < width; x++) {
      if (!checkAndCreateBombAction(x, yPos, levelObject)) {
        break;
      }
    }

    for (int y = yPos - 1; y > yPos - bombRange && y >= 0; y--) {
      if (!checkAndCreateBombAction(xPos, y, levelObject)) {
        break;
      }
    }

    for (int y = yPos + 1; y < yPos + bombRange && y < height; y++) {
      if (!checkAndCreateBombAction(xPos, y, levelObject)) {
        break;
      }
    }
  }

  public boolean checkAndCreateBombAction(final int x, final int y, final LevelObject levelObject) {
    if (x < 0 || y < 0) {
      return false;
    }
    if (objectAt(x, y) != LevelObject.WALL) {
      setLevelField(x, y, levelObject);
      return true;
    } else {
      return false;
    }
  }

  public void setLevelField(final int x, final int y, final LevelObject levelObject) {
    level[y][x] = levelObject.getValue();
  }

  public LevelObject objectAt(final int x, final int y) {
    return LevelObject.fromInteger(level[y][x]);
  }

  public boolean validMove(final MoveDto moveDto, final PlayerDto playerDto) {
    final Rectangle moveRectangle = new Rectangle(moveDto, height);
    if (moveRectangle.outOfBounds(width * MapConst.TEXTURE_SIZE, height * MapConst.TEXTURE_SIZE)) {
      return false;
    }

    final Rectangle playerRectangle = new Rectangle(playerDto, height);

    if (moveRectangle.collidesWith(level, LevelObject.WALL)) {
      return false;
    }

    return moveRectangle.triesToLeaveObject(level, LevelObject.BOMB, playerRectangle);
  }

  private int formatPositionX(final float pos) {
    final int roundPos = (int) pos;
    return roundPos / MapConst.TEXTURE_SIZE;
  }

  private int formatPositionY(final float pos) {
    final int roundPos = (int) pos;
    final int formatted = roundPos / MapConst.TEXTURE_SIZE;
    return height - 1 - formatted;
  }
}
