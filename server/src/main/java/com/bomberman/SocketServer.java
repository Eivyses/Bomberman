package com.bomberman;

import com.bomberman.entities.Position;
import com.bomberman.game.Game;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

public class SocketServer {
  private final Game game;
  private SocketIOServer server;

  public SocketServer(final Game game) {
    this.game = game;

    initServer();
  }

  private static String getPlayerId(final SocketIOClient client) {
    return client.getSessionId().toString();
  }

  private void initServer() {
    final var config = new Configuration();
    config.setPort(5050);

    server = new SocketIOServer(config);

    addEventListeners();

    server.start();
    System.out.println("socket server has started");
  }

  private void addEventListeners() {
    server.addEventListener(
        "connectNewPlayer",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);

          game.addPlayer(playerId);

          client.sendEvent("connectNewPlayerSuccess", playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });

    server.addEventListener(
        "disconnectPlayer",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.removePlayer(playerId);

          client.sendEvent("disconnectPlayerSuccess");
        });

    server.addEventListener(
        "movePlayer",
        Position.class,
        (client, position, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.movePlayer(playerId, position);

          client.sendEvent("movePlayerSuccess");
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });

    server.addEventListener(
        "placeBomb",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);

          game.placeBomb(playerId);
          client.sendEvent("placeBombSuccess");
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
  }
}
