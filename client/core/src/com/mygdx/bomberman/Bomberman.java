package com.mygdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.bomberman.entities.FrameRate;
import com.mygdx.bomberman.game.Drawer;
import com.mygdx.bomberman.game.Game;
import com.mygdx.bomberman.game.SocketClient;

public class Bomberman extends ApplicationAdapter {
  private SocketClient socketClient;
  private Game game;
  private Drawer drawer;

  private SpriteBatch batch;
  private FrameRate frameRate;

  @Override
  public void create() {
    batch = new SpriteBatch();
    frameRate = new FrameRate(batch);

    game = new Game();
    drawer = new Drawer(batch);

    socketClient = new SocketClient(game);
  }

  @Override
  public void render() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    final var player = game.getCurrentPlayer();
    final var speed = 120f;

    final var dt = Gdx.graphics.getDeltaTime();
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      socketClient.move(player.getPosition().getX(), player.getPosition().getY() + dt * speed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      socketClient.move(player.getPosition().getX(), player.getPosition().getY() - dt * speed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      socketClient.move(player.getPosition().getX() - dt * speed, player.getPosition().getY());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      socketClient.move(player.getPosition().getX() + dt * speed, player.getPosition().getY());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
      socketClient.move(
          player.getPosition().getX() + dt * speed, player.getPosition().getY() + dt * speed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      socketClient.move(
          player.getPosition().getX() + dt * speed, player.getPosition().getY() - dt * speed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
      socketClient.move(
          player.getPosition().getX() - dt * speed, player.getPosition().getY() + dt * speed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      socketClient.move(
          player.getPosition().getX() - dt * speed, player.getPosition().getY() - dt * speed);
    }
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      socketClient.placeBomb();
    }

    batch.begin();

    drawer.drawGame(game.getGameState());

    frameRate.update();
    frameRate.draw();

    batch.end();
  }

  @Override
  public void dispose() {
    batch.dispose();
    socketClient.disconnectPlayer();
  }
}
