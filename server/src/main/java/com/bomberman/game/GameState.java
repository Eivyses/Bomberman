package com.bomberman.game;

import com.bomberman.entities.mapobject.Bomb;
import com.bomberman.entities.mapobject.BombExplosion;
import com.bomberman.entities.mapobject.Brick;
import com.bomberman.entities.mapobject.MapObject;
import com.bomberman.entities.mapobject.Wall;
import com.bomberman.entities.mapobject.movable.Player;
import com.bomberman.entities.mapobject.pickup.BombPickup;
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
  private final List<BombPickup> bombPickups;

  public GameState() {
    players = new ArrayList<>();
    bombs = new ArrayList<>();
    walls = new ArrayList<>();
    bombExplosions = new ArrayList<>();
    bricks = new ArrayList<>();
    bombPickups = new ArrayList<>();
  }

  public List<BombPickup> getBombPickups() {
    return bombPickups;
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
