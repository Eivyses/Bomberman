package com.mygdx.bomberman.drawings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.bomberman.entities.Position;

/** @author Satalia team. */
public abstract class TextDrawable implements Disposable {
  private final BitmapFont font;
  private final SpriteBatch batch;
  private OrthographicCamera cam;
  private String text;
  private Position position;

  TextDrawable(final SpriteBatch batch, final String text, final Position position) {
    this.text = text;
    font = new BitmapFont();
    this.batch = batch;
    cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.position = position;
  }

  public void resize(final int screenWidth, final int screenHeight) {
    final float oldWidth = cam.viewportWidth;
    final float oldHeight = cam.viewportHeight;

    final float newX = screenWidth / oldWidth * position.getX();
    final float newY = screenHeight / oldHeight * position.getY();
    position = new Position(newX, newY);

    cam = new OrthographicCamera(screenWidth, screenHeight);
    cam.translate(screenWidth / 2, screenHeight / 2);
    cam.update();
    batch.setProjectionMatrix(cam.combined);
  }

  public abstract void update();

  void setText(final String text) {
    this.text = text;
  }

  public void updateAndDraw() {
    update();
    draw();
  }

  private void draw() {
    font.draw(batch, text, position.getX(), position.getY());
  }

  @Override
  public void dispose() {
    font.dispose();
    batch.dispose();
  }
}
