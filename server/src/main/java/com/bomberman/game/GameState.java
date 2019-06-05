package com.bomberman.game;

import com.bomberman.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameState {
  private final List<Player> players;
  private final List<Bomb> bombs;
  private final List<Wall> walls;
  private final List<BombExplosion> bombExplosions;

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

  public List<MapObject> getObstacles() {
    return new ArrayList<>(walls);
//    return Stream.concat(walls.stream(), bombs.stream()).collect(Collectors.toList());
  }
}
