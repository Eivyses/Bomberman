package com.mygdx.bomberman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    private float x;
    private float y;
    private int maxWidth;
    private int maxHeigth;
    private String id;

    private Texture texture;
    private SpriteBatch batch;

    private float speed;

    public Player(String id, Texture texture, SpriteBatch batch) {
        this.texture = texture;
        this.id = id;
        x = 0;
        y = 0;
        speed = 120.0f;
        this.batch = batch;

        maxWidth = Gdx.graphics.getWidth() - texture.getWidth();
        maxHeigth = Gdx.graphics.getHeight() - texture.getHeight();
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeigth() {
        return maxHeigth;
    }

    public String getId() {
        return id;
    }

    public Texture getTexture() {
        return texture;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void draw() {
        batch.draw(texture, x, y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void moveUp(float dt) {
        y += dt * speed;
        if (y > maxHeigth) {
            y = maxHeigth;
        }
    }

    public void moveDown(float dt) {
        y -= dt * speed;
        if (y < 0) {
            y = 0.0f;
        }
    }

    public void moveRight(float dt) {
        x += dt * speed;
        if (x > maxWidth) {
            x = maxWidth;
        }
    }

    public void moveLeft(float dt) {
        x -= dt * speed;
        if (x < 0) {
            x = 0.0f;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
