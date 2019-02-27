package com.bomberman;

import com.bomberman.Dto.LevelDto;
import com.bomberman.Dto.MoveDto;
import com.bomberman.Dto.PlayerDto;
import com.bomberman.Dto.PlayersDto;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BombermanLauncher {

    public static void main(String[] args) throws InterruptedException {
        PlayersDto players = new PlayersDto();
        LevelDto levelDto = new LevelDto();

        var config = new Configuration();
        config.setPort(5050);

        final var server = new SocketIOServer(config);

        server.addEventListener("connection", String.class, (socketIOClient, data, ackRequest) -> {
            System.out.println("new client connected");
            System.out.println(socketIOClient.getSessionId());
            var playerDto = new PlayerDto(0, 0, socketIOClient.getSessionId().toString());

            players.getPlayers().put(playerDto.getId(), playerDto);
            socketIOClient.sendEvent("user", playerDto);
            socketIOClient.sendEvent("otherPlayers", players);
            socketIOClient.sendEvent("map", levelDto);
            server.getBroadcastOperations().sendEvent("newPlayerConnected", playerDto);
        });

        server.addEventListener("disconnected", PlayerDto.class, (socketIOClient, playerDto, ackRequest) -> {
            System.out.println("player disconnected " + playerDto.getId());
            players.getPlayers().remove(playerDto.getId());
            server.getBroadcastOperations().sendEvent("otherPlayers", players);
        });

        server.addEventListener("move", MoveDto.class, (socketIOClient, moveDto, ackRequest) -> {
            var player = players.getPlayers().get(socketIOClient.getSessionId().toString());
            if (moveDto.getX() < 0) {
                moveDto.setX(0);
            }
            if (moveDto.getY() < 0) {
                moveDto.setY(0);
            }
            if (levelDto.validMove(moveDto.getX(), moveDto.getY(), player.getX(), player.getY())) {
//                System.out.println("moving to: " + moveDto.getX() + " : " + moveDto.getY());
                player.setX(moveDto.getX());
                player.setY(moveDto.getY());
            } else {
//                System.out.println("Invalid move: " + moveDto.getX() + " : " + moveDto.getY());
//                System.out.println("Real values: " + player.getX() + " : " + player.getY());
                return;
            }
            server.getBroadcastOperations().sendEvent("playerMoved", player);
        });

        server.addEventListener("bombPlaced", MoveDto.class, (socketIOClient, moveDto, ackRequest) -> {
            var player = players.getPlayers().get(socketIOClient.getSessionId().toString());
            if (levelDto.canPlaceBomb(moveDto.getX(), moveDto.getY())) {
                levelDto.placeBomb(moveDto.getX(), moveDto.getY());
                server.getBroadcastOperations().sendEvent("map", levelDto);
                System.out.println("BOMB PLACED");
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                executor.schedule(() -> {
                    levelDto.removeBomb(moveDto.getX(), moveDto.getY());
                    server.getBroadcastOperations().sendEvent("map", levelDto);

                    System.out.println("BOOM FUCKER");
                }, 3, TimeUnit.SECONDS);

            }
        });

        server.start();
        System.out.println("started");
        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
