package com.bomberman.zilvinasGame;

import com.bomberman.entities.Position;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private PlayerFactory playerFactory;
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Wall> walls;

    public Game() {
        playerFactory = new PlayerFactory();
        players = new ArrayList<>();
        bombs = new ArrayList<>();
        walls = new ArrayList<>();
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

    public void placeBomb(Position position) {
        bombs.add(new Bomb(position));
    }

    public void initGame() {
        walls.add(new Wall(new Position(2f, 2f)));
    }
}
