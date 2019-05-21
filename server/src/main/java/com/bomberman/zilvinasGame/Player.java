package com.bomberman.zilvinasGame;

import com.bomberman.entities.Position;

public class Player {

    private String id;
    private Position position;

    public Player(String id, Position position) {
        this.id = id;
        this.position = position;
    }

    public void setPosition(Position position) {

    }

    public String getId() {
        return id;
    }
}
