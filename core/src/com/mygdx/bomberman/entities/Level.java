package com.mygdx.bomberman.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mygdx.bomberman.dto.LevelDto;

public class Level {
    private static final Texture WALL_TEXTURE = new Texture("textures\\Wall.png");
    private static final Texture TERRAIN_TEXTURE = new Texture("textures\\Terrain.png");
    private static final Texture BOMB_TEXTURE = new Texture("textures\\Bomb.png");
    private Batch batch;
    private int[][] level;
    private int height;
    private int width;

    public Level(Batch batch) {
        this.batch = batch;
    }

    public void draw() {
        for (int x = getWidth() - 1; x >= 0; x--) {
            for (int y = getHeight() - 1; y >= 0; y--) {
                int tile = getLevel()[y][x];
                switch (tile) {
                    case LevelDto.TERRAIN: {
                        batch.draw(TERRAIN_TEXTURE, x * 32, (getHeight() - 1 - y) * 32);
                        break;
                    }
                    case LevelDto.WALL: {
                        batch.draw(WALL_TEXTURE, x * 32, (getHeight() - 1 - y) * 32);
                        break;
                    }
                    case LevelDto.BOMB: {
                        batch.draw(TERRAIN_TEXTURE, x * 32, (getHeight() - 1 - y) * 32);
                        batch.draw(BOMB_TEXTURE, x * 32, (getHeight() - 1 - y) * 32);
                        break;
                    }
                    default:
                        break;
                }
            }
        }
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

    public void setLevel(LevelDto levelDto) {
        this.level = levelDto.getLevel();
        this.height = levelDto.getHeight();
        this.width = levelDto.getWidth();
    }
}