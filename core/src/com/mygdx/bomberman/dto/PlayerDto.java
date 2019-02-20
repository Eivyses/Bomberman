package com.mygdx.bomberman.dto;

import com.mygdx.bomberman.entities.Player;

public class PlayerDto {
    private float x;
    private float y;
    private String id;

    public PlayerDto(float x, float y, String id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public PlayerDto() {
    }

    public PlayerDto(Player player) {
        this.x = player.getX();
        this.y = player.getY();
        this.id = player.getId();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
