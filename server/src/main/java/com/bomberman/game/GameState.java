package com.bomberman.game;

import com.bomberman.entities.Bomb;
import com.bomberman.entities.BombExplosion;
import com.bomberman.entities.Brick;
import com.bomberman.entities.MapObject;
import com.bomberman.entities.Player;
import com.bomberman.entities.Wall;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameState {
  private final List<Player> players;
  private final List<Bomb> bombs;
  private final List<Wall> walls;
  private final List<BombExplosion> bombExplosions;
  private final List<Brick> bricks;

  public GameState() {
    players = new ArrayList<>();
    bombs = new ArrayList<>();
    walls = new ArrayList<>();
    bombExplosions = new ArrayList<>();
    bricks = new ArrayList<>();
  }

  public List<Brick> getBricks() {
    return bricks;
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
    return Stream.concat(walls.stream(), bricks.stream()).collect(Collectors.toList());
  }
}
