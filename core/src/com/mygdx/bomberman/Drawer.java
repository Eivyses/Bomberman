package com.mygdx.bomberman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.zilvinasEntities.Bomb;
import com.mygdx.bomberman.zilvinasEntities.GameState;
import com.mygdx.bomberman.zilvinasEntities.Player;
import com.mygdx.bomberman.zilvinasEntities.Wall;

import java.util.List;

public class Drawer {
    private Texture bluePlayer;
    private Texture bombTexture;
    private Texture wallTexture;

    private SpriteBatch batch;

    public Drawer(SpriteBatch batch) {
        this.batch = batch;

        bluePlayer = new Texture("textures\\Bomberman.png");
        bombTexture = new Texture("textures\\Bomb.png");
        wallTexture = new Texture("textures\\Wall.png");
    }

    public void drawGame(GameState gameState) {
        // TODO: Does not work, somehow position is null
        // drawWalls(gameState.getWalls());
        drawBombs(gameState.getBombs());
        drawPlayers(gameState.getPlayers());
    }

    private void drawPlayers(List<Player> players) {
        players.forEach(this::drawPlayer);
    }

    private void drawPlayer(Player player) {
        batch.draw(bluePlayer, player.getPosition().getX(), player.getPosition().getY());
    }

    private void drawBombs(List<Bomb> bombs) {
        bombs.forEach(this::drawBomb);
    }

    private void drawBomb(Bomb bomb) {
        batch.draw(bombTexture, bomb.getPosition().getX(), bomb.getPosition().getY());
    }

    private void drawWalls(List<Wall> walls) {
        walls.forEach(this::drawWall);
    }

    private void drawWall(Wall wall) {
        var x = wall.getPosition().getX();
        var y = wall.getPosition().getY();
        batch.draw(wallTexture, x * 32, y * 32);
    }
}
