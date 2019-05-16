package com.mygdx.bomberman.dto;

import com.google.gson.Gson;
import com.mygdx.bomberman.entities.Position;
import org.json.JSONException;
import org.json.JSONObject;

public class MoveDto {
  private final Position position;

  public MoveDto(final Position position) {
    this.position = position;
  }

  public MoveDto(final float x, final float y) {
    this.position = new Position(x, y);
  }

  public Position getPosition() {
    return position;
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
