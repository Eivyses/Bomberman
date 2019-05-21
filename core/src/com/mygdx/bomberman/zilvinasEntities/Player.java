package com.mygdx.bomberman.zilvinasEntities;


import com.mygdx.bomberman.entities.Position;

public class Player {

    private String id;
    private Position position;
    private int bombDurationInSeconds = 2;

    public Player(String id, Position position) {
        this.id = id;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {

    }

    public int getBombDurationInSeconds() {
        return bombDurationInSeconds;
    }
}
