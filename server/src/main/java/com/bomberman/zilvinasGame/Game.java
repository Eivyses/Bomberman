package com.bomberman.zilvinasGame;

import com.bomberman.entities.Position;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private PlayerFactory playerFactory;
    private GameState gameState;

    public Game() {
        playerFactory = new PlayerFactory();
        gameState = new GameState();
    }

    public String addPlayer(String id) {
        var newPlayer = playerFactory.createPlayer(id);

        gameState.getPlayers().add(newPlayer);

        return newPlayer.getId();
    }

    public void removePlayer(String id) {
        gameState.getPlayers().removeIf(x -> x.getId().equals(id));
    }

    public void movePlayer(String playerId, Position position) {
        var player = gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

        player.ifPresent(value -> value.setPosition(position));
    }

    public void placeBomb(String playerId) {
        var currentTime = ZonedDateTime.now();
        var player = getPlayer(playerId);
        var bombDurationInSeconds = player.getBombDurationInSeconds();
        var position = player.getPosition();
        var explosionTime = currentTime.plusSeconds(bombDurationInSeconds);

        gameState.getBombs().add(new Bomb(position, explosionTime));
    }

    public void initGame() {
        gameState.getWalls().add(new Wall(new Position(32f, 32f)));
    }

    private Player getPlayer(String playerId) {
        return gameState.getPlayers().stream()
                .filter(x -> x.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    public GameState getGameState() {
        return gameState;
    }
}
