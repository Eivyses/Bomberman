package com.mygdx.bomberman.dto;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class MoveDto {
    private float x;
    private float y;

    public MoveDto(float x, float y) {
        this.x = x;
        this.y = y;
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

    public Object toJson() {
        try {
            return new JSONObject(new Gson().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
