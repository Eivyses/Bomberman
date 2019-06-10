package com.mygdx.bomberman.drawings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.bomberman.entities.Position;

/**
 * A nicer class for showing framerate that doesn't spam the console like Logger.log()
 *
 * @author William Hartman
 */
public class FrameRate extends TextDrawable {
  private long lastTimeCounted;
  private float sinceChange;
  private float frameRate;

  public FrameRate(final SpriteBatch batch) {
    this(batch, " initializing", new Position(3, Gdx.graphics.getHeight() - 3));
    frameRate = Gdx.graphics.getFramesPerSecond();
  }

  private FrameRate(final SpriteBatch batch, final String text, final Position position) {
    super(batch, text, position);
  }

  @Override
  public void update() {
    final long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
    lastTimeCounted = TimeUtils.millis();

    sinceChange += delta;
    if (sinceChange >= 1000) {
      sinceChange = 0;
      frameRate = Gdx.graphics.getFramesPerSecond();
      super.setText((int) frameRate + " fps");
    }
  }
}
