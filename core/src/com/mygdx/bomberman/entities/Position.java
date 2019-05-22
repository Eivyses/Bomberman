package com.mygdx.bomberman.entities;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class Position {
  private final float x;
  private final float y;

  public Position(final float x, final float y) {
    this.x = x < 0 ? 0 : x;
    this.y = y < 0 ? 0 : y;
  }

  public Position() {
    this.x = 0f;
    this.y = 0f;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public Object toJson() {
    try {
      return new JSONObject(new Gson().toJson(this));
    } catch (final JSONException e) {
      e.printStackTrace();
    }
    return null;
  }
}
