package com.mygdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.mygdx.bomberman.dto.Constants;
import com.mygdx.bomberman.dto.PlayerDto;
import com.mygdx.bomberman.dto.User;
import com.mygdx.bomberman.entities.FrameRate;
import com.mygdx.bomberman.entities.Player;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Bomberman extends ApplicationAdapter {
    Texture bluePlayer;
    private SpriteBatch batch;
    private Socket socket;
    private Player player;
    private List<Player> players;
    private FrameRate frameRate;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bluePlayer = new Texture("textures\\Bomberman.png");
//        player = new Player(bluePlayer, batch);
        frameRate = new FrameRate(batch);
        players = new ArrayList<>();
        connectSocket();
        configSocketEvents();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.moveUp(Gdx.graphics.getDeltaTime());
            sendKeyPress();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.moveDown(Gdx.graphics.getDeltaTime());
            sendKeyPress();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft(Gdx.graphics.getDeltaTime());
            sendKeyPress();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight(Gdx.graphics.getDeltaTime());
            sendKeyPress();
        }

        batch.begin();
        frameRate.update();
        frameRate.draw();
        for (Player p : players) {
            p.draw();
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void connectSocket() {
        try {
            socket = IO.socket(Constants.getInstance().url + ":" + Constants.getInstance().port);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void configSocketEvents() {
        // connect
        socket.on(Socket.EVENT_CONNECT, args -> System.out.println("Socket connected"))
                // get user id
                .on(Constants.getInstance().userIdString, args -> {
                    User user = new Gson().fromJson(args[0].toString(), User.class);
                    System.out.println("User Id: " + user.getId());
                })
                // get user object
                .on("user", args -> {
                    PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
                    player = new Player(playerDto.getId(), bluePlayer, batch);
                    players.add(player);
                })
                // get player moved
                .on("userMove", args -> {
                    PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
                    Player p = findPlayerIndex(playerDto.getId());
                    if (p == null) {
                        p = new Player(playerDto.getId(), bluePlayer, batch);
                        players.add(p);
                    }
                    p.setX(playerDto.getX());
                    p.setY(playerDto.getY());

//                System.out.println("User " + keyReceived.getId() + " pressed button: " + keyReceived.getKey());
                });
    }

    private Player findPlayerIndex(String id) {
        for (Player p : players) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    private void sendKeyPress() {
        PlayerDto playerDto = new PlayerDto(player);
        socket.emit("move", new Gson().toJson(playerDto));
    }
}
