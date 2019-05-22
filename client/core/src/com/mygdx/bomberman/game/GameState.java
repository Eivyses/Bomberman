package com.mygdx.bomberman.game;

import com.mygdx.bomberman.entities.Bomb;
import com.mygdx.bomberman.entities.BombExplosion;
import com.mygdx.bomberman.entities.Player;
import com.mygdx.bomberman.entities.Wall;

import java.util.ArrayList;
import java.util.List;

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
}
