package com.bomberman.utils;

import com.bomberman.Dto.MoveDto;
import com.bomberman.Dto.PlayerDto;
import com.bomberman.LevelObject;

public class Rectangle {

  private int leftBotX;
  private int leftBotY;

  private int rightTopX;
  private int rightTopY;

  private int leftTopX;
  private int leftTopY;

  private int rightBotX;
  private int rightBotY;

  private float x;
  private float y;

  public Rectangle(MoveDto moveDto, int height) {
    x = moveDto.getX();
    y = moveDto.getY();
    fillRectangle(x, y, height);
  }

  public Rectangle(PlayerDto playerDto, int height) {
    x = playerDto.getX();
    y = playerDto.getY();
    fillRectangle(x, y, height);
  }

  private void fillRectangle(float x, float y, int height) {
    leftBotX = formatPlayerPositionX(x, true);
    leftBotY = formatPlayerPositionY(y, true, height);

    rightTopX = formatPlayerPositionX(x, false);
    rightTopY = formatPlayerPositionY(y, false, height);

    leftTopX = formatPlayerPositionX(x, true);
    leftTopY = formatPlayerPositionY(y, false, height);

    rightBotX = formatPlayerPositionX(x, false);
    rightBotY = formatPlayerPositionY(y, true, height);
  }

  public boolean collidesWith(int[][] level, LevelObject levelObject) {
    return collides(level, rightTopX, rightTopY, levelObject)
        || collides(level, rightBotX, rightBotY, levelObject)
        || collides(level, leftTopX, leftTopY, levelObject)
        || collides(level, leftBotX, leftBotY, levelObject);
  }

  public boolean triesToLeaveObject(
      int[][] level, LevelObject levelObject, Rectangle startRectangle) {
    boolean objectRightTop = collides(level, rightTopX, rightTopY, levelObject);
    boolean objectRightBot = collides(level, rightBotX, rightBotY, levelObject);
    boolean objectLeftTop = collides(level, leftTopX, leftTopY, levelObject);
    boolean objectLeftBot = collides(level, leftBotX, leftBotY, levelObject);

    // TODO: if two bombs placed, user can just walk out... fix
    // in a center of object, allow to go everywhere
    if (objectRightTop && objectLeftBot && objectRightBot && objectLeftTop) {
      return true;
    }
    if (objectLeftTop && objectRightTop) {
      return y <= startRectangle.y;
    }
    if (objectLeftTop && objectLeftBot) {
      return x >= startRectangle.x;
    }
    if (objectLeftBot && objectRightBot) {
      return y >= startRectangle.y;
    }
    if (objectRightTop && objectRightBot) {
      return x <= startRectangle.x;
    }
    //        System.out.println(rightTopX + " : " + rightTopY);
    return true;
  }

  private boolean collides(int[][] level, int x, int y, LevelObject levelObject) {
    return level[y][x] == levelObject.getValue();
  }

  private int formatPlayerPositionX(float pos, boolean start) {
    int roundPos = (int) pos;
    if (roundPos == 0) {
      return 0;
    }
    int ending = start ? 0 : 14;
    int formatted = (roundPos + ending) / 32;
    return formatted;
  }

  private int formatPlayerPositionY(float pos, boolean start, int height) {
    int roundPos = (int) pos;
    int ending = start ? 0 : 22;
    int formatted = (roundPos + ending) / 32;
    return height - 1 - formatted;
  }
}
