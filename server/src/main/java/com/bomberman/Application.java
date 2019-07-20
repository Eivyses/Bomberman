package com.bomberman;

import com.bomberman.game.Game;

public class Application {
  public static void main(final String[] args) {
    final var game = new Game();

    new SocketServer(game);
  }
}
