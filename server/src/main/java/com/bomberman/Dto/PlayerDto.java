package com.bomberman.Dto;


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

