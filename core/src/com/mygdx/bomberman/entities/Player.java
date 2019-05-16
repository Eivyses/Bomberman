package com.mygdx.bomberman.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.dto.PlayerDto;

public class Player extends MovableObject {
  private final int maxWidth;
  private final int maxHeight;
  private final String id;
  private boolean moved;
  private int bombRange;

  private final Texture texture;
  private final SpriteBatch batch;

  private float speed;

  public Player(final PlayerDto playerDto, final Texture texture, final SpriteBatch batch) {
    super(playerDto.getPosition());
    this.texture = texture;
    this.id = playerDto.getId();
    speed = 120.0f;
    this.batch = batch;
    this.bombRange = 3;

    maxWidth = Gdx.graphics.getWidth() - texture.getWidth();
    maxHeight = Gdx.graphics.getHeight() - texture.getHeight();
  }

  public boolean isMoved() {
    return moved;
  }

  public int getBombRange() {
    return bombRange;
  }

  public void setBombRange(final int bombRange) {
    this.bombRange = bombRange;
  }

  public void setMoved(final boolean moved) {
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
    batch.draw(texture, getPosition().getX(), getPosition().getY());
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(final float speed) {
    this.speed = speed;
  }
}
