package com.mygdx.bomberman.game;

import com.mygdx.bomberman.entities.mapobject.Bomb;
import com.mygdx.bomberman.entities.mapobject.BombExplosion;
import com.mygdx.bomberman.entities.mapobject.Brick;
import com.mygdx.bomberman.entities.mapobject.Player;
import com.mygdx.bomberman.entities.mapobject.Wall;
import com.mygdx.bomberman.entities.mapobject.pickup.Pickup;
import java.util.ArrayList;
import java.util.List;

public class GameState {
  private final List<Player> players;
  private final List<Bomb> bombs;
  private final List<Wall> walls;
  private final List<BombExplosion> bombExplosions;
  private final List<Brick> bricks;
  private final List<Pickup> pickups;

  public GameState() {
    players = new ArrayList<>();
    bombs = new ArrayList<>();
    walls = new ArrayList<>();
    bombExplosions = new ArrayList<>();
    bricks = new ArrayList<>();
    pickups = new ArrayList<>();
  }

  public List<Pickup> getPickups() {
    return pickups;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public List<Bomb> getBombs() {
    return bombs;
  }

  public List<Brick> getBricks() {
    return bricks;
  }

  public List<Wall> getWalls() {
    return walls;
  }

  public List<BombExplosion> getBombExplosions() {
    return bombExplosions;
  }
}
