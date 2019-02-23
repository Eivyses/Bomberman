package com.mygdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mygdx.bomberman.dto.Constants;
import com.mygdx.bomberman.dto.PlayerDto;
import com.mygdx.bomberman.entities.FrameRate;
import com.mygdx.bomberman.entities.Player;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bomberman extends ApplicationAdapter {
    private Texture bluePlayer;
    private SpriteBatch batch;
    private Socket socket;
    private Player player;
    private Map<String, Player> players;
    private FrameRate frameRate;

    @Override
    public void create() {
        batch = new SpriteBatch();
        bluePlayer = new Texture("textures\\Bomberman.png");
        frameRate = new FrameRate(batch);
        players = new HashMap<>();
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
        for (Map.Entry<String, Player> p : players.entrySet()) {
            p.getValue().draw();
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
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
        socket.on(Socket.EVENT_CONNECT, args -> System.out.println("Socket connected"))
                // get other players
                .on("otherPlayers", args -> {
                    List<PlayerDto> playerDtoList = new Gson().fromJson(args[0].toString(), new TypeToken<List<PlayerDto>>() {
                    }.getType());
                    for (PlayerDto playerDto : playerDtoList) {
                        Player pl = new Player(playerDto, bluePlayer, batch);
                        players.put(pl.getId(), pl);
                    }
                })
                // new user connected
                .on("newPlayerConnected", args -> {
                    PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
                    System.out.println("Player connected: " + playerDto.getId());
                    Player p = new Player(playerDto, bluePlayer, batch);
                    players.put(p.getId(), p);
                })
                // get user object
                .on("user", args -> {
                    PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
                    player = new Player(playerDto, bluePlayer, batch);
                    players.put(playerDto.getId(), player);
                })
                // get player moved
                .on("userMove", args -> {
                    PlayerDto playerDto = new Gson().fromJson(args[0].toString(), PlayerDto.class);
                    Player p = players.get(playerDto.getId());
                    p.setX(playerDto.getX());
                    p.setY(playerDto.getY());
                });
    }

    private void sendKeyPress() {
        PlayerDto playerDto = new PlayerDto(player);

        try {
            JSONObject obj = new JSONObject(new Gson().toJson(playerDto));
            socket.emit("move", obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
