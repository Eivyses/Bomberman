package com.bomberman;

import com.bomberman.Dto.BombDto;
import com.bomberman.Dto.BombsDto;
import com.bomberman.Dto.LevelDto;
import com.bomberman.Dto.MoveDto;
import com.bomberman.Dto.PlayerDto;
import com.bomberman.Dto.PlayersDto;
import com.bomberman.zilvinasGame.Game;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BombermanLauncher {

    public static String getPlayerId(SocketIOClient client) {
        return client.getSessionId().toString();
    }

    public static void main(final String[] args) throws InterruptedException {
        final PlayersDto players = new PlayersDto();
        final LevelDto levelDto = new LevelDto();
        final BombsDto bombsDto = new BombsDto();

        final var config = new Configuration();
        config.setPort(5050);

        final var server = new SocketIOServer(config);

        var game = new Game();

        server.addEventListener(
            "connection",
            String.class,
            (socketIOClient, data, ackRequest) -> {
                var playerId = getPlayerId(socketIOClient);

                System.out.println("new client connected");
                System.out.println(playerId);

                final var playerDto = new PlayerDto(0, 0, socketIOClient.getSessionId().toString());

                players.getPlayers().put(playerDto.getId(), playerDto);
                socketIOClient.sendEvent("user", playerDto);
                socketIOClient.sendEvent("otherPlayers", players);
                socketIOClient.sendEvent("map", levelDto);
                socketIOClient.sendEvent("bombPlaced", bombsDto);
                server.getBroadcastOperations().sendEvent("newPlayerConnected", playerDto);

                game.addPlayer(playerId);
                socketIOClient.sendEvent("game", game);
                socketIOClient.sendEvent("newPlayerConnected", playerId);
            });

        server.addEventListener(
            "disconnected",
            PlayerDto.class,
            (socketIOClient, playerDto, ackRequest) -> {
                System.out.println("player disconnected " + playerDto.getId());
                players.getPlayers().remove(playerDto.getId());
                server.getBroadcastOperations().sendEvent("otherPlayers", players);

                var playerId = getPlayerId(socketIOClient);
                game.removePlayer(playerId);
                socketIOClient.sendEvent("game", game);
            });
        server.addEventListener(
            "move",
            MoveDto.class,
            (socketIOClient, moveDto, ackRequest) -> {
                var playerId = getPlayerId(socketIOClient);

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

                game.movePlayer(playerId, moveDto.getPosition());
                socketIOClient.sendEvent("game", game);
            });

        server.addEventListener(
            "bombPlace",
            PlayerDto.class,
            (socketIOClient, playerDto, ackRequest) -> {
                var playerId = getPlayerId(socketIOClient);

                game.placeBomb(playerId);
                socketIOClient.sendEvent("game", game);
                socketIOClient.sendEvent("bombPlaced");

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
