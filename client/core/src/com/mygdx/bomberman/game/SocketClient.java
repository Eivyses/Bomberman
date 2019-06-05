package com.mygdx.bomberman.game;

import com.google.gson.Gson;
import com.mygdx.bomberman.constants.Constants;
import com.mygdx.bomberman.entities.Position;
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

  public void move(final float x, final float y) {
    final Position position = new Position(x, y);
    socket.emit("movePlayer", position.toJson());
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
    // get player object for new player
    //                .on(
    //                        "user",
    //                        args -> {
    //                            PlayerDto playerDto = new Gson().fromJson(args[0].toString(),
    // PlayerDto.class);
    //                            player = new Player(playerDto, bluePlayer, batch);
    //                            players.put(playerDto.getId(), player);
    //                        })
    //                // get other players for new player
    //                .on(
    //                        "otherPlayers",
    //                        args -> {
    //                            PlayersDto playersDto = new Gson().fromJson(args[0].toString(),
    // PlayersDto.class);
    //                            players.clear();
    //                            playersDto
    //                                    .getPlayers()
    //                                    .forEach((key, value) -> players.put(key, new
    // Player(value, bluePlayer, batch)));
    //                        })
    //                // get map for new player
    //                .on(
    //                        "map",
    //                        args -> {
    //                            LevelDto levelDto = new Gson().fromJson(args[0].toString(),
    // LevelDto.class);
    //                            level.setLevel(levelDto);
    //                        })
    //                // bomb placed
    //                .on(
    //                        "bombPlaced",
    //                        args -> {
    //                            final BombsDto bombsDto = new Gson().fromJson(args[0].toString(),
    // BombsDto.class);
    //                            bombs.clear();
    //                            bombsDto.getBombs().forEach(bomb -> bombs.add(new Bomb(bomb,
    // bombTexture, batch)));
    //                        })
    //                // bomb exploded
    //                .on(
    //                        "bombExploded",
    //                        args -> {
    //                            final BombsDto bombsDto = new Gson().fromJson(args[0].toString(),
    // BombsDto.class);
    //                            bombs.clear();
    //                            bombsDto.getBombs().forEach(bomb -> bombs.add(new Bomb(bomb,
    // bombTexture, batch)));
    //                        })
    //                // get player moved
    //                .on(
    //                        "playerMoved",
    //                        args -> {
    //                            final PlayerDto playerDto = new
    // Gson().fromJson(args[0].toString(), PlayerDto.class);
    //                            Player p = players.get(playerDto.getId());
    //                            if (p == null) {
    //                                p = new Player(playerDto, bluePlayer, batch);
    //                            }
    //                            // hack
    //                            if (player.getId().equals(p.getId())) {
    //                                player = p;
    //                            }
    //                            p.setPosition(playerDto.getPosition());
    //                        });
  }
}
