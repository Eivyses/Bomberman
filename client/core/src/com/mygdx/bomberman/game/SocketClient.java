package com.mygdx.bomberman.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mygdx.bomberman.constants.Configuration;
import com.mygdx.bomberman.deserializer.PickupDeserializer;
import com.mygdx.bomberman.entities.Direction;
import com.mygdx.bomberman.entities.Movement;
import com.mygdx.bomberman.entities.mapobject.pickup.Pickup;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;

public class SocketClient {
  private Socket socket;
  private final Game game;
  private final Gson gson;

  public SocketClient(final Game game) {
    gson = new GsonBuilder().registerTypeAdapter(Pickup.class, new PickupDeserializer()).create();
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
      socket = IO.socket(Configuration.URL + ":" + Configuration.PORT);
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
              String playerId = gson.fromJson(args[0].toString(), String.class);
              game.setPlayerId(playerId);
            })
        .on(
            "getGameState",
            args -> {
              final GameState gameState = gson.fromJson(args[0].toString(), GameState.class);
              game.setGameState(gameState);
            });
  }

  public void increaseBombRange() {
    socket.emit("increaseBombRange");
  }

  public void decreaseBombRange() {
    socket.emit("decreaseBombRange");
  }

  public void increasePlayerSpeed() {
    socket.emit("increasePlayerSpeed");
  }

  public void decreasePlayerSpeed() {
    socket.emit("decreasePlayerSpeed");
  }

  public void increaseMaxBombCount() {
    socket.emit("increaseMaxBombCount");
  }
}
