package com.mygdx.bomberman.game;

import com.google.gson.Gson;
import com.mygdx.bomberman.constants.Constants;
import com.mygdx.bomberman.entities.Direction;
import com.mygdx.bomberman.entities.Movement;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

public class SocketClient {
  private Socket socket;
  private final Game game;

  public SocketClient(final Game game) {
    this.game = game;

    connectSocket();
    configureListeners();
  }

  public void move(final Direction direction, final float dt) {
    final Movement movement = new Movement(direction, dt);
    socket.emit("movePlayer", movement.toJson());
  }

  public void respawnPlayer() {
    socket.emit("respawnPlayer");
  }

  public void placeBomb() {
    socket.emit("placeBomb");
  }

  public void disconnectPlayer() {
    socket.emit("disconnectPlayer");
  }

  private void connectSocket() {
    try {
      socket = IO.socket(Constants.url + ":" + Constants.port);
      socket.connect();
    } catch (final URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private void configureListeners() {
    socket
        .on(
            Socket.EVENT_CONNECT,
            args -> {
              System.out.println("Socket connected");
              socket.emit("connectNewPlayer");
            })
        .on(
            "connectNewPlayerSuccess",
            args -> {
              String playerId = new Gson().fromJson(args[0].toString(), String.class);
              game.setPlayerId(playerId);
            })
        .on(
            "getGameState",
            args -> {
              //                            System.out.println("Get game state");
              final GameState gameState = new Gson().fromJson(args[0].toString(), GameState.class);
              game.setGameState(gameState);
            });
  }
}
