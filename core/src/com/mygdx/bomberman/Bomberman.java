package com.mygdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.mygdx.bomberman.dto.*;
import com.mygdx.bomberman.entities.Bomb;
import com.mygdx.bomberman.entities.FrameRate;
import com.mygdx.bomberman.entities.Level;
import com.mygdx.bomberman.entities.Player;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bomberman extends ApplicationAdapter {
  private Texture bluePlayer;
  private Texture bombTexture;
  private SpriteBatch batch;
  private Socket socket;
  private Player player;
  private Map<String, Player> players;
  private List<Bomb> bombs;
  private FrameRate frameRate;
  private Level level;

  @Override
  public void create() {
    batch = new SpriteBatch();
    bluePlayer = new Texture("textures\\Bomberman.png");
    bombTexture = new Texture("textures\\Bomb.png");
    frameRate = new FrameRate(batch);
    players = new HashMap<>();
    level = new Level(batch);
    bombs = new ArrayList<>();
    connectSocket();
    configSocketEvents();
  }

  @Override
  public void render() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (player != null) {
      player.setMoved(false);
    }
    final float dt = Gdx.graphics.getDeltaTime();
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      move(player.getPosition().getX(), player.getPosition().getY() + dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      move(player.getPosition().getX(), player.getPosition().getY() - dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      move(player.getPosition().getX() - dt * player.getSpeed(), player.getPosition().getY());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      move(player.getPosition().getX() + dt * player.getSpeed(), player.getPosition().getY());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
      move(
          player.getPosition().getX() + dt * player.getSpeed(),
          player.getPosition().getY() + dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      move(
          player.getPosition().getX() + dt * player.getSpeed(),
          player.getPosition().getY() - dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
      move(
          player.getPosition().getX() - dt * player.getSpeed(),
          player.getPosition().getY() + dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      move(
          player.getPosition().getX() - dt * player.getSpeed(),
          player.getPosition().getY() - dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      placeBomb(player);
    }

    batch.begin();
    if (level != null) {
      level.draw();
    }
    bombs.forEach(Bomb::draw);
    players.forEach((key, value) -> value.draw());

    frameRate.update();
    frameRate.draw();
    batch.end();
  }

  private void move(final float x, final float y) {
    final MoveDto moveDto = new MoveDto(x, y);
    socket.emit("move", moveDto.toJson());
  }

  private void placeBomb(final Player player) {
    final PlayerDto playerDto = new PlayerDto(player);
    socket.emit("bombPlace", playerDto.toJson());
  }

  @Override
  public void dispose() {
    batch.dispose();
    final PlayerDto playerDto = new PlayerDto(player);
    socket.emit("disconnected", playerDto.toJson());
  }

  private void connectSocket() {
    try {
      socket = IO.socket(Constants.url + ":" + Constants.port);
      socket.connect();
    } catch (final URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private void configSocketEvents() {
    // connect
    socket
        .on(
            Socket.EVENT_CONNECT,
            args -> {
              System.out.println("Socket connected");
              socket.emit("connection", "me");
              System.out.println("Socket emitted");
            })
        // new user connected
        .on(
            "newPlayerConnected",
            args -> {
              PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
              System.out.println("Player connected: " + playerDto.getId());
              Player p = new Player(playerDto, bluePlayer, batch);
              players.put(p.getId(), p);
            })
        // get player object for new player
        .on(
            "user",
            args -> {
              PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
              player = new Player(playerDto, bluePlayer, batch);
              players.put(playerDto.getId(), player);
            })
        // get other players for new player
        .on(
            "otherPlayers",
            args -> {
              PlayersDto playersDto = new Gson().fromJson(args[0].toString(), PlayersDto.class);
              players.clear();
              playersDto
                  .getPlayers()
                  .forEach((key, value) -> players.put(key, new Player(value, bluePlayer, batch)));
            })
        // get map for new player
        .on(
            "map",
            args -> {
              LevelDto levelDto = new Gson().fromJson(args[0].toString(), LevelDto.class);
              level.setLevel(levelDto);
            })
        // bomb placed
        .on(
            "bombPlaced",
            args -> {
              final BombsDto bombsDto = new Gson().fromJson(args[0].toString(), BombsDto.class);
              bombs.clear();
              bombsDto.getBombs().forEach(bomb -> bombs.add(new Bomb(bomb, bombTexture, batch)));
            })
        // bomb exploded
        .on(
            "bombExploded",
            args -> {
              final BombsDto bombsDto = new Gson().fromJson(args[0].toString(), BombsDto.class);
              bombs.clear();
              bombsDto.getBombs().forEach(bomb -> bombs.add(new Bomb(bomb, bombTexture, batch)));
            })
        // get player moved
        .on(
            "playerMoved",
            args -> {
              final PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
              Player p = players.get(playerDto.getId());
              if (p == null) {
                p = new Player(playerDto, bluePlayer, batch);
              }
              // hack
              if (player.getId().equals(p.getId())) {
                player = p;
              }
              p.setPosition(playerDto.getPosition());
            });
  }
}
