package com.bomberman.utils;

import com.bomberman.Dto.MoveDto;
import com.bomberman.Dto.PlayerDto;
import com.bomberman.LevelObject;
import com.bomberman.constants.MapConst;
import com.bomberman.entities.Position;

public class Rectangle {

  private Position leftBotPos;
  private Position rightTopPos;
  private Position leftTopPos;
  private Position rightBotPos;

  private final Position realPos;

  public Rectangle(final MoveDto moveDto, final int height) {
    realPos = moveDto.getPosition();
    fillRectangle(realPos, height);
  }

  public Rectangle(final PlayerDto playerDto, final int height) {
    realPos = playerDto.getPosition();
    fillRectangle(realPos, height);
  }

  private void fillRectangle(final Position realPos, final int height) {
    int x = formatPlayerPositionX(realPos.getX(), true);
    int y = formatPlayerPositionY(realPos.getY(), true, height);
    leftBotPos = new Position(x, y);

    x = formatPlayerPositionX(realPos.getX(), false);
    y = formatPlayerPositionY(realPos.getY(), false, height);
    rightTopPos = new Position(x, y);

    x = formatPlayerPositionX(realPos.getX(), true);
    y = formatPlayerPositionY(realPos.getY(), false, height);
    leftTopPos = new Position(x, y);

    x = formatPlayerPositionX(realPos.getX(), false);
    y = formatPlayerPositionY(realPos.getY(), true, height);
    rightBotPos = new Position(x, y);
  }

  public boolean outOfBounds(final float boundX, final float boundY) {
    return realPos.getX() + 14 > boundX
        || realPos.getY() + 22 > boundY
        || realPos.getY() < 0
        || realPos.getX() < 0;
  }

  public boolean collidesWith(final int[][] level, final LevelObject levelObject) {
    return collides(level, rightTopPos, levelObject)
        || collides(level, rightBotPos, levelObject)
        || collides(level, leftBotPos, levelObject)
        || collides(level, leftTopPos, levelObject);
  }

  public boolean triesToLeaveObject(
      final int[][] level, final LevelObject levelObject, final Rectangle startRectangle) {
    final boolean objectRightTop = collides(level, rightTopPos, levelObject);
    final boolean objectRightBot = collides(level, rightBotPos, levelObject);
    final boolean objectLeftTop = collides(level, leftTopPos, levelObject);
    final boolean objectLeftBot = collides(level, leftBotPos, levelObject);

    // TODO: if two bombs placed, user can just walk out... fix
    // in a center of object, allow to go everywhere
    if (objectRightTop && objectLeftBot && objectRightBot && objectLeftTop) {
      return true;
    }
    if (objectLeftTop && objectRightTop) {
      return realPos.getY() <= startRectangle.realPos.getY();
    }
    if (objectLeftTop && objectLeftBot) {
      return realPos.getX() >= startRectangle.realPos.getX();
    }
    if (objectLeftBot && objectRightBot) {
      return realPos.getY() >= startRectangle.realPos.getY();
    }
    if (objectRightTop && objectRightBot) {
      return realPos.getX() <= startRectangle.realPos.getX();
    }
    //        System.out.println(rightTopX + " : " + rightTopY);
    return true;
  }

  private boolean collides(final int[][] level, final Position pos, final LevelObject levelObject) {
    final int x = (int) pos.getX();
    final int y = (int) pos.getY();
    return level[y][x] == levelObject.getValue();
  }

  private int formatPlayerPositionX(final float pos, final boolean start) {
    final int roundPos = (int) pos;
    if (roundPos == 0) {
      return 0;
    }
    final int ending = start ? 0 : 14;
    return (roundPos + ending) / MapConst.TEXTURE_SIZE;
  }

  private int formatPlayerPositionY(final float pos, final boolean start, final int height) {
    final int roundPos = (int) pos;
    final int ending = start ? 0 : 22;
    final int formatted = (roundPos + ending) / MapConst.TEXTURE_SIZE;
    return height - 1 - formatted;
  }
}
