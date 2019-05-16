package com.bomberman;

import com.bomberman.Dto.*;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BombermanLauncher {

  public static void main(final String[] args) throws InterruptedException {
    final PlayersDto players = new PlayersDto();
    final LevelDto levelDto = new LevelDto();
    final BombsDto bombsDto = new BombsDto();

    final var config = new Configuration();
    config.setPort(5050);

    final var server = new SocketIOServer(config);

    server.addEventListener(
        "connection",
        String.class,
        (socketIOClient, data, ackRequest) -> {
          System.out.println("new client connected");
          System.out.println(socketIOClient.getSessionId());
          final var playerDto = new PlayerDto(0, 0, socketIOClient.getSessionId().toString());

          players.getPlayers().put(playerDto.getId(), playerDto);
          socketIOClient.sendEvent("user", playerDto);
          socketIOClient.sendEvent("otherPlayers", players);
          socketIOClient.sendEvent("map", levelDto);
          socketIOClient.sendEvent("bombPlaced", bombsDto);
          server.getBroadcastOperations().sendEvent("newPlayerConnected", playerDto);
        });

    server.addEventListener(
        "disconnected",
        PlayerDto.class,
        (socketIOClient, playerDto, ackRequest) -> {
          System.out.println("player disconnected " + playerDto.getId());
          players.getPlayers().remove(playerDto.getId());
          server.getBroadcastOperations().sendEvent("otherPlayers", players);
        });
    server.addEventListener(
        "move",
        MoveDto.class,
        (socketIOClient, moveDto, ackRequest) -> {
          final PlayerDto player =
              players.getPlayers().get(socketIOClient.getSessionId().toString());
          if (levelDto.validMove(moveDto, player)) {
            //                System.out.println("moving to: " + moveDto.getX() + " : " +
            // moveDto.getY());
            player.setPosition(moveDto.getPosition());
          } else {
            //                System.out.println("Invalid move: " + moveDto.getX() + " : " +
            // moveDto.getY());
            //                System.out.println("Real values: " + player.getX() + " : " +
            // player.getY());
            return;
          }
          server.getBroadcastOperations().sendEvent("playerMoved", player);
        });

    server.addEventListener(
        "bombPlace",
        PlayerDto.class,
        (socketIOClient, playerDto, ackRequest) -> {
          final PlayerDto player =
              players.getPlayers().get(socketIOClient.getSessionId().toString());
          if (player.canPlaceBomb(bombsDto.getBombs())) {
            final BombDto bomb = player.placeBomb();
            bombsDto.getBombs().add(bomb);
            server.getBroadcastOperations().sendEvent("bombPlaced", bombsDto);
            System.out.println("BOMB PLACED");
            final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
            executor.schedule(
                () -> {
                  bombsDto.getBombs().remove(bomb);
                  levelDto.createBombAction(bomb, LevelObject.EXPLOSION);
                  socketIOClient.sendEvent("map", levelDto);
                  server.getBroadcastOperations().sendEvent("bombPlaced", bombsDto);
                  //                  server.getBroadcastOperations().sendEvent("map", levelDto);
                  System.out.println("BOOM FUCKER");
                  final ScheduledThreadPoolExecutor explosionExecutor =
                      new ScheduledThreadPoolExecutor(1);
                  explosionExecutor.schedule(
                      () -> {
                        levelDto.createBombAction(bomb, LevelObject.TERRAIN);
                        socketIOClient.sendEvent("map", levelDto);
                      },
                      1,
                      TimeUnit.SECONDS);
                },
                3,
                TimeUnit.SECONDS);
          }
        });

    server.start();
    System.out.println("started");
    Thread.sleep(Integer.MAX_VALUE);

    server.stop();
  }
}
