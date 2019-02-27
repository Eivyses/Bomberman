package com.mygdx.bomberman.dto;

public class LevelDto {

    public static final int WALL = 1;
    public static final int TERRAIN = 0;
    public static final int BOMB = 2;
    private int[][] level;
    private int height;
    private int width;

    public LevelDto() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getLevel() {
        return level;
    }
}
