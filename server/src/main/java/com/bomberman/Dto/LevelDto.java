package com.bomberman.Dto;

public class LevelDto {
    public static final int WALL = 1;
    public static final int TERRAIN = 0;
    public static final int BOMB = 2;

    private int width;
    private int height;
    private int[][] level;

    public LevelDto() {
        width = 15;
        height = 11;
        initLevel();
    }

    private void initLevel() {
        level = //15x11

                new int[][]{
                        {
                                1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0
                        },
                        {
                                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0
                        },
                        {
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },
                        {
                                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0
                        },
                        {
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0
                        },
                        {
                                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0
                        },
                        {
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0
                        },
                        {
                                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0
                        },
                        {
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        },
                        {
                                0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0
                        },
                        {
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1
                        }

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

    public boolean validMove(float x, float y, float fromX, float fromY) {
        var leftBotX = formatPlayerPositionX(x, true);
        var leftBotY = formatPlayerPositionY(y, true);

        var rightTopX = formatPlayerPositionX(x, false);
        var rightTopY = formatPlayerPositionY(y, false);

        var leftTopX = formatPlayerPositionX(x, true);
        var leftTopY = formatPlayerPositionY(y, false);

        var rightBotX = formatPlayerPositionX(x, false);
        var rightBotY = formatPlayerPositionY(y, true);

        boolean isWall = isWall(rightTopX, rightTopY)
                || isWall(rightBotX, rightBotY)
                || isWall(leftTopX, leftTopY)
                || isWall(leftBotX, leftBotY);

        if (isWall) {
            return false;
        }

        boolean bombRightTop = isBomb(rightTopX, rightTopY);
        boolean bombRightBot = isBomb(rightBotX, rightBotY);
        boolean bombLeftTop = isBomb(leftTopX, leftTopY);
        boolean bombLeftBot = isBomb(leftBotX, leftBotY);

        // in a center of bomb, allow to go everywhere
        if (bombRightTop && bombLeftBot && bombRightBot && bombLeftTop) {
            return true;
        }
        if (bombLeftTop && bombRightTop) {
            return y <= fromY;
        }
        if (bombLeftTop && bombLeftBot) {
            return x >= fromX;
        }
        if (bombLeftBot && bombRightBot) {
            return y >= fromY;
        }
        if (bombRightTop && bombRightBot) {
            return x <= fromX;
        }
//        System.out.println(rightTopX + " : " + rightTopY);
        return true;
    }

    private boolean isWall(int x, int y) {
        return level[y][x] == WALL;
    }

    private boolean isBomb(int x, int y) {
        return level[y][x] == BOMB;
    }

    public void placeBomb(float x, float y) {
        int gridX = formatPositionX(x);
        int gridY = formatPositionY(y);
        setValue(gridX, gridY, BOMB);
    }

    public void removeBomb(float x, float y){
        int gridX = formatPositionX(x);
        int gridY = formatPositionY(y);
        setValue(gridX, gridY, TERRAIN);
    }

    public boolean canPlaceBomb(float x, float y) {
        int gridX = formatPositionX(x);
        int gridY = formatPositionY(y);
        return level[gridY][gridX] != BOMB;
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

    private int formatPlayerPositionX(float pos, boolean start) {
        int roundPos = (int) pos;
        if (roundPos == 0) {
            return 0;
        }
        int ending = start ? 0 : 14;
        int formatted = (roundPos + ending) / 32;
        return formatted;
    }

    private int formatPlayerPositionY(float pos, boolean start) {
        int roundPos = (int) pos;
        int ending = start ? 0 : 22;
        int formatted = (roundPos + ending) / 32;
        return height - 1 - formatted;
    }
}
