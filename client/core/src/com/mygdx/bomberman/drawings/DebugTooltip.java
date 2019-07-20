package com.mygdx.bomberman.drawings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.entities.Position;

public class DebugTooltip extends TextDrawable {

  public DebugTooltip(final SpriteBatch batch) {
    super(batch, "", new Position(300, Gdx.graphics.getHeight() - 3));
  }

  @Override
  public void update() {
    super.setText(
        "F1 - show/hide this menu\n"
            + "F2 - increase bomb range\n"
            + "F3 - decrease bomb range\n"
            + "F4 - increase player speed\n"
            + "F5 - decrease player speed\n"
            + "F6 - respawn player");
  }
}
