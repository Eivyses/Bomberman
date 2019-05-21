package com.bomberman;

import com.bomberman.zilvinasGame.Game;
import com.corundumstudio.socketio.Configuration;

public class BombermanLauncher {
    public static void main(final String[] args) {
        var config = new Configuration();
        config.setPort(5050);

        var game = new Game();
        game.initGame();

        new SocketServer(game);
    }
}
