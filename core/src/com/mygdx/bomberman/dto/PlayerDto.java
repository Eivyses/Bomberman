package com.mygdx.bomberman.dto;

import com.google.gson.Gson;
import com.mygdx.bomberman.entities.MovableObject;
import com.mygdx.bomberman.entities.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerDto extends MovableObject {
  private String id;

  public PlayerDto(final float x, final float y, final String id) {
    super(x, y);
    this.id = id;
  }

  public PlayerDto() {
    super();
  }

  public PlayerDto(final Player player) {
    super(player.getPosition());
    this.id = player.getId();
  }

  public Object toJson() {
    try {
      return new JSONObject(new Gson().toJson(this));
    } catch (final JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }
}
