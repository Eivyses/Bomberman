package com.mygdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.mygdx.bomberman.dto.*;
import com.mygdx.bomberman.entities.FrameRate;
import com.mygdx.bomberman.entities.Level;
import com.mygdx.bomberman.entities.Player;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Bomberman extends ApplicationAdapter {
  private Texture bluePlayer;
  private SpriteBatch batch;
  private Socket socket;
  private Player player;
  private Map<String, Player> players;
  private FrameRate frameRate;
  private Level level;

  @Override
  public void create() {
    batch = new SpriteBatch();
    bluePlayer = new Texture("textures\\Bomberman.png");
    frameRate = new FrameRate(batch);
    players = new HashMap<>();
    level = new Level(batch);
    connectSocket();
    configSocketEvents();
  }

  @Override
  public void render() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (player != null) {
      player.setMoved(false);
    }
    float dt = Gdx.graphics.getDeltaTime();
    if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
      move(player.getX(), player.getY() + dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      move(player.getX(), player.getY() - dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
      move(player.getX() - dt * player.getSpeed(), player.getY());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
      move(player.getX() + dt * player.getSpeed(), player.getY());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
      move(player.getX() + dt * player.getSpeed(), player.getY() + dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      move(player.getX() + dt * player.getSpeed(), player.getY() - dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
      move(player.getX() - dt * player.getSpeed(), player.getY() + dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
      move(player.getX() - dt * player.getSpeed(), player.getY() - dt * player.getSpeed());
    }
    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      placeBomb(player.getX(), player.getY());
    }

    batch.begin();
    if (level != null) {
      level.draw();
    }
    frameRate.update();
    frameRate.draw();
    for (Map.Entry<String, Player> p : players.entrySet()) {
      p.getValue().draw();
    }
    batch.end();
  }

  private void move(float x, float y) {
    MoveDto moveDto = new MoveDto(x, y);
    socket.emit("move", moveDto.toJson());
  }

  private void placeBomb(float x, float y) {
    MoveDto moveDto = new MoveDto(x, y);
    socket.emit("bombPlaced", moveDto.toJson());
  }

  @Override
  public void dispose() {
    batch.dispose();
    PlayerDto playerDto = new PlayerDto(player);
    socket.emit("disconnected", playerDto.toJson());
  }

  private void connectSocket() {
    try {
      socket = IO.socket(Constants.url + ":" + Constants.port);
      socket.connect();
    } catch (URISyntaxException e) {
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
        // get player moved
        .on(
            "playerMoved",
            args -> {
              PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
              Player p = players.get(playerDto.getId());
              if (p == null) {
                p = new Player(playerDto, bluePlayer, batch);
              }
              // hack
              if (player.getId().equals(p.getId())) {
                player = p;
              }
              p.setX(playerDto.getX());
              p.setY(playerDto.getY());
            });
  }
}
