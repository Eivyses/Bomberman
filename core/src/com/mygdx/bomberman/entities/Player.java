package com.mygdx.bomberman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.dto.PlayerDto;

public class Player {
  private float x;
  private float y;
  private int maxWidth;
  private int maxHeight;
  private String id;
  private boolean moved;

  private Texture texture;
  private SpriteBatch batch;

  private float speed;

  public Player(PlayerDto playerDto, Texture texture, SpriteBatch batch) {
    this.texture = texture;
    this.id = playerDto.getId();
    x = playerDto.getX();
    y = playerDto.getY();
    speed = 120.0f;
    this.batch = batch;

    maxWidth = Gdx.graphics.getWidth() - texture.getWidth();
    maxHeight = Gdx.graphics.getHeight() - texture.getHeight();
  }

  public boolean isMoved() {
    return moved;
  }

  public void setMoved(boolean moved) {
    this.moved = moved;
  }

  public int getMaxWidth() {
    return maxWidth;
  }

  public int getMaxHeight() {
    return maxHeight;
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
    if (y > maxHeight) {
      y = maxHeight;
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
