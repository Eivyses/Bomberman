package com.mygdx.bomberman.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.bomberman.constants.Configuration;
import com.mygdx.bomberman.entities.mapobject.Bomb;
import com.mygdx.bomberman.entities.mapobject.BombExplosion;
import com.mygdx.bomberman.entities.mapobject.Brick;
import com.mygdx.bomberman.entities.mapobject.MapObject;
import com.mygdx.bomberman.entities.mapobject.Player;
import com.mygdx.bomberman.entities.mapobject.Wall;
import java.util.List;

public class Drawer {
  private final Texture bluePlayer;
  private final Texture bombTexture;
  private final Texture bombExplosionTexture;
  private final Texture wallTexture;
  private final Texture terrainTexture;
  private final Texture movingPlayerSheet;
  private final Texture brickTexture;
  private final Animation<TextureRegion> movingPlayer;
  private final SpriteBatch batch;

  private static final int FRAME_COLS = 3, FRAME_ROWS = 2;

  public Drawer(final SpriteBatch batch) {
    this.batch = batch;

    movingPlayerSheet = new Texture("textures\\bomberman_sprite.png");
    bluePlayer = new Texture("textures\\Bomberman.png");
    bombTexture = new Texture("textures\\Bomb.png");
    bombExplosionTexture = new Texture("textures\\Explosion.png");
    wallTexture = new Texture("textures\\Wall.png");
    terrainTexture = new Texture("textures\\Terrain.png");
    brickTexture = new Texture("textures\\Brick.png");

    final TextureRegion[][] tmp =
        TextureRegion.split(
            movingPlayerSheet,
            movingPlayerSheet.getWidth() / FRAME_COLS,
            movingPlayerSheet.getHeight() / FRAME_ROWS);

    final TextureRegion[] animationFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
    int index = 0;
    for (int i = 0; i < FRAME_ROWS; i++) {
      for (int j = 0; j < FRAME_COLS; j++) {
        animationFrames[index++] = tmp[i][j];
      }
    }

    movingPlayer = new Animation<>(0.075f, animationFrames);
  }

  public void drawGame(final GameState gameState) {
    drawTerrain();
    drawBricks(gameState.getBricks());
    drawExplosions(gameState.getBombExplosions());
    drawWalls(gameState.getWalls());
    drawBombs(gameState.getBombs());
    drawPlayers(gameState.getPlayers());
  }

  private void drawBricks(final List<Brick> bricks) {
    bricks.forEach(this::drawBrick);
  }

  private void drawBrick(final Brick brick) {
    drawBaseTexture(brickTexture, brick);
  }

  private void drawPlayers(final List<Player> players) {
    players.forEach(this::drawPlayer);
  }

  private void drawExplosions(final List<BombExplosion> explosions) {
    explosions.forEach(this::drawExplosion);
  }

  private void drawExplosion(final BombExplosion explosion) {
    drawBaseTexture(bombExplosionTexture, explosion);
  }

  private void drawPlayer(final Player player) {
    if (player.isDead()) {
      drawBaseTexture(bombExplosionTexture, player);
    } else {
      drawPlayerTexture(player);
    }
  }

  private void drawBombs(final List<Bomb> bombs) {
    bombs.forEach(this::drawBomb);
  }

  private void drawBomb(final Bomb bomb) {
    drawBaseTexture(bombTexture, bomb);
  }

  private void drawWalls(final List<Wall> walls) {
    walls.forEach(this::drawWall);
  }

  private void drawWall(final Wall wall) {
    drawBaseTexture(wallTexture, wall);
  }

  private void drawTerrain() {
    batch.draw(terrainTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
  }

  private void drawBaseTexture(final Texture texture, final MapObject mapObject) {
    final float x = mapObject.getPosition().getX();
    final float y = mapObject.getPosition().getY();
    /* so, what's happening here?
     * server calculates everything using it's on grid system, for ex. simple map wall or
     * brick has a size of 32x32
     * What happens when we move that value to front?
     * We need to show a texture on it based on current resolution. that's what this function
     * is doing. it finds real starting point for object by knowing how many of those should be
     * present in line or row and then stretches texture based on resolution.
     * */
    final float objectWidthScaled = Gdx.graphics.getWidth() / Configuration.GAME_COLUMNS;
    final float objectHeightScaled = Gdx.graphics.getHeight() / Configuration.GAME_ROWS;
    batch.draw(
        texture,
        x / Configuration.TEXTURE_SIZE * objectWidthScaled,
        y / Configuration.TEXTURE_SIZE * objectHeightScaled,
        objectWidthScaled,
        objectHeightScaled);
  }

  private void drawPlayerTexture(final Player player) {
    final float x = player.getPosition().getX();
    final float y = player.getPosition().getY();
    final TextureRegion currentFrame = movingPlayer.getKeyFrame(player.getStateTime(), true);
    final float playerHeightScaled =
        Gdx.graphics.getHeight()
            / (Configuration.TEXTURE_SIZE
                / Configuration.PLAYER_TEXTURE_HEIGHT
                * Configuration.GAME_ROWS);
    final float playerWidthScaled =
        Gdx.graphics.getWidth()
            / (Configuration.TEXTURE_SIZE
                / Configuration.PLAYER_TEXTURE_WIDTH
                * Configuration.GAME_COLUMNS);

    batch.draw(
        currentFrame,
        x / Configuration.PLAYER_TEXTURE_WIDTH * playerWidthScaled,
        y / Configuration.PLAYER_TEXTURE_HEIGHT * playerHeightScaled,
        playerWidthScaled,
        playerHeightScaled);
  }
}
