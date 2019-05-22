package com.bomberman;

import com.bomberman.game.Game;
import com.corundumstudio.socketio.Configuration;

public class Application {
  public static void main(final String[] args) {
    final var config = new Configuration();
    config.setPort(5050);

    final var game = new Game();
    game.initGame();

    new SocketServer(game);
  }
}
