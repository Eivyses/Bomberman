package com.mygdx.bomberman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.bomberman.Bomberman;

public class DesktopLauncher {
  public static void main(final String[] arg) {
    final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 1280;
    config.height = 720;

    new LwjglApplication(new Bomberman(), config);
  }
}
