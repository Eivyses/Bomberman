package com.mygdx.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.mygdx.bomberman.dto.Constants;
import com.mygdx.bomberman.dto.KeyReceived;
import com.mygdx.bomberman.dto.User;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class Bomberman extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    private Socket socket;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

        connectSocket();
        configSocketEvents();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            sendKeyPress("UP");
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            sendKeyPress("DOWN");
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sendKeyPress("LEFT");
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sendKeyPress("RIGHT");
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
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
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Socket connected");
            }
        // get user id
        }).on(Constants.getInstance().userIdString, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                User user = new Gson().fromJson(args[0].toString(), User.class);
                System.out.println("User Id: " + user.getId());
            }
        // get key pressed
        }).on(Constants.getInstance().keyServerString, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                KeyReceived keyReceived = new Gson().fromJson(args[0].toString(), KeyReceived.class);
                System.out.println("User " + keyReceived.getId() + " pressed button: " + keyReceived.getKey());
            }
        });
    }

    private void sendKeyPress(String key) {
        socket.emit(Constants.getInstance().keyClientString, key);
    }
}
