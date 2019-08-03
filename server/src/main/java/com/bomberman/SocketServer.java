package com.bomberman;

import com.bomberman.entities.Movement;
import com.bomberman.entities.mapobject.Bomb;
import com.bomberman.game.Game;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class SocketServer {
  private final Game game;
  private SocketIOServer server;

  SocketServer(final Game game) {
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
          System.out.println("New player connected: " + playerId);
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
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });

    server.addEventListener(
        "movePlayer",
        Movement.class,
        (client, movement, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.movePlayer(playerId, movement);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });

    server.addEventListener(
        "placeBomb",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          final var bomb = game.placeBomb(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
          bomb.ifPresent(this::setBombExplosion);
        });

    // DEBUG
    server.addEventListener(
        "respawnPlayer",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.respawnPlayer(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
    server.addEventListener(
        "increaseBombRange",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);

          game.increaseBombRange(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
    server.addEventListener(
        "decreaseBombRange",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.decreaseBombRange(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
    server.addEventListener(
        "increasePlayerSpeed",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.increasePlayerSpeed(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
    server.addEventListener(
        "decreasePlayerSpeed",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.decreasePlayerSpeed(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
    server.addEventListener(
        "increaseMaxBombCount",
        String.class,
        (client, data, ackRequest) -> {
          final var playerId = getPlayerId(client);
          game.increaseMaxBombCount(playerId);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        });
  }

  private void setBombExplosion(final Bomb bomb) {
    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    executor.schedule(
        () -> {
          game.explodeBomb(bomb, bomb.getBombId(), bomb.getPlayerId());
          game.checkKilled(bomb);
          removeBombExplosion(bomb);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        },
        bomb.getExplosionTime(),
        TimeUnit.SECONDS);
  }

  private void removeBombExplosion(final Bomb bomb) {
    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    executor.schedule(
        () -> {
          game.removeExplosions(bomb);
          server.getBroadcastOperations().sendEvent("getGameState", game.getGameState());
        },
        1,
        TimeUnit.SECONDS);
  }
}
