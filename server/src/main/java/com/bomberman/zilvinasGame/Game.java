package com.bomberman.zilvinasGame;

import com.bomberman.entities.Position;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private PlayerFactory playerFactory;
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Wall> walls;
    private List<BombExplosion> bombExplosions;

    public Game() {
        playerFactory = new PlayerFactory();
        players = new ArrayList<>();
        bombs = new ArrayList<>();
        walls = new ArrayList<>();
        bombExplosions = new ArrayList<>();
    }

    public String addPlayer(String id) {
        var newPlayer = playerFactory.createPlayer(id);

        players.add(newPlayer);

        return newPlayer.getId();
    }

    public void removePlayer(String id) {
        players.removeIf(x -> x.getId().equals(id));
    }

    public void movePlayer(String playerId, Position position) {
        var player = players.stream().filter(x -> x.getId().equals(playerId)).findFirst();

        player.ifPresent(value -> value.setPosition(position));
    }

    public void placeBomb(String playerId) {
        var currentTime = ZonedDateTime.now();
        var player = getPlayer(playerId);
        var bombDurationInSeconds = player.getBombDurationInSeconds();
        var position = player.getPosition();
        var explosionTime = currentTime.plusSeconds(bombDurationInSeconds);

        bombs.add(new Bomb(position, explosionTime));
    }

    public void initGame() {
        walls.add(new Wall(new Position(32f, 32f)));
    }

    private Player getPlayer(String playerId) {
        return players.stream()
                .filter(x -> x.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }
}
