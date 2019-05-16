package com.mygdx.bomberman.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.dto.BombDto;

public class Bomb extends MovableObject {
  private int bombRange;
  private final Texture texture;
  private final SpriteBatch batch;

  public Bomb(final BombDto bombDto, final Texture texture, final SpriteBatch batch) {
    super(bombDto.getPosition());
    this.texture = texture;
    this.batch = batch;
    this.bombRange = bombDto.getBombRange();
  }

  public void draw() {
    batch.draw(texture, getPosition().getX(), getPosition().getY());
  }

  public int getBombRange() {
    return bombRange;
  }

  public void setBombRange(final int bombRange) {
    this.bombRange = bombRange;
  }
}
