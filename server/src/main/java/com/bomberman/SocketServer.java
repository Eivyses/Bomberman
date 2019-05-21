package com.bomberman;

import com.bomberman.Dto.MoveDto;
import com.bomberman.zilvinasGame.Game;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

public class SocketServer {
    private Game game;
    private SocketIOServer server;

    public SocketServer(Game game) {
        this.game = game;

        initServer();
    }

    private static String getPlayerId(SocketIOClient client) {
        return client.getSessionId().toString();
    }

    private void initServer() {
        var config = new Configuration();
        config.setPort(5050);

        server = new SocketIOServer(config);

        addEventListeners();

        server.start();
        System.out.println("socket server has started");
    }

    private void addEventListeners() {
        server.addEventListener("connectNewPlayer", String.class, (client, data, ackRequest) -> {
            var playerId = getPlayerId(client);

            game.addPlayer(playerId);

            client.sendEvent("connectNewPlayerSuccess", playerId);
            client.sendEvent("getGameState", game);
        });

        server.addEventListener(
                "disconnectPlayer",
                String.class,
                (client, data, ackRequest) -> {
                    var playerId = getPlayerId(client);
                    game.removePlayer(playerId);

                    client.sendEvent("disconnectPlayerSuccess");
                });

        server.addEventListener(
                "movePlayer",
                MoveDto.class,
                (client, moveDto, ackRequest) -> {
                    var playerId = getPlayerId(client);
                    game.movePlayer(playerId, moveDto.getPosition());

                    client.sendEvent("movePlayerSuccess");
                });

        server.addEventListener(
                "placeBomb",
                String.class,
                (socketIOClient, data, ackRequest) -> {
                    var playerId = getPlayerId(socketIOClient);

                    game.placeBomb(playerId);
                    socketIOClient.sendEvent("placeBombSuccess");
                });
    }
}
