package com.mygdx.bomberman.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.entities.Bomb;
import com.mygdx.bomberman.entities.BombExplosion;
import com.mygdx.bomberman.entities.Player;
import com.mygdx.bomberman.entities.Wall;
import java.util.List;

public class Drawer {
  private final Texture bluePlayer;
  private final Texture bombTexture;
  private final Texture bombExplosionTexture;
  private final Texture wallTexture;
  private final Texture terrainTexture;

  private final SpriteBatch batch;

  public Drawer(final SpriteBatch batch) {
    this.batch = batch;

    bluePlayer = new Texture("textures\\Bomberman.png");
    bombTexture = new Texture("textures\\Bomb.png");
    bombExplosionTexture = new Texture("textures\\Explosion.png");
    wallTexture = new Texture("textures\\Wall.png");
    terrainTexture = new Texture("textures\\Terrain.png");
  }

  public void drawGame(final GameState gameState) {
    drawTerrain();
    drawExplosions(gameState.getBombExplosions());
    drawWalls(gameState.getWalls());
    drawBombs(gameState.getBombs());
    drawPlayers(gameState.getPlayers());
  }

  private void drawPlayers(final List<Player> players) {
    players.forEach(this::drawPlayer);
  }

  private void drawExplosions(final List<BombExplosion> explosions) {
    explosions.forEach(this::drawExplosion);
  }

  private void drawExplosion(final BombExplosion explosion) {
    batch.draw(
        bombExplosionTexture, explosion.getPosition().getX(), explosion.getPosition().getY());
  }

  private void drawPlayer(final Player player) {
    if (player.isDead()) {
      batch.draw(bombExplosionTexture, player.getPosition().getX(), player.getPosition().getY());
    } else {
      batch.draw(bluePlayer, player.getPosition().getX(), player.getPosition().getY());
    }
  }

  private void drawBombs(final List<Bomb> bombs) {
    bombs.forEach(this::drawBomb);
  }

  private void drawBomb(final Bomb bomb) {
    batch.draw(bombTexture, bomb.getPosition().getX(), bomb.getPosition().getY());
  }

  private void drawWalls(final List<Wall> walls) {
    walls.forEach(this::drawWall);
  }

  private void drawWall(final Wall wall) {
    final float x = wall.getPosition().getX();
    final float y = wall.getPosition().getY();
    batch.draw(wallTexture, x, y);
  }

  private void drawTerrain() {
    batch.draw(terrainTexture, 0, 0, 480, 352);
  }
}
