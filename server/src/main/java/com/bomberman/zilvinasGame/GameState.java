package com.bomberman.zilvinasGame;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Wall> walls;
    private List<BombExplosion> bombExplosions;

    public GameState() {
        players = new ArrayList<>();
        bombs = new ArrayList<>();
        walls = new ArrayList<>();
        bombExplosions = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<BombExplosion> getBombExplosions() {
        return bombExplosions;
    }
}
