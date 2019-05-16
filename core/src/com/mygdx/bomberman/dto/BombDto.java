package com.mygdx.bomberman.dto;

import com.google.gson.Gson;
import com.mygdx.bomberman.entities.Bomb;
import com.mygdx.bomberman.entities.MovableObject;
import org.json.JSONException;
import org.json.JSONObject;

public class BombDto extends MovableObject {
  private int bombRange;

  public BombDto() {
    super();
  }

  public BombDto(final Bomb bomb) {
    super(bomb.getPosition());
    this.bombRange = bomb.getBombRange();
  }

  public int getBombRange() {
    return bombRange;
  }

  public void setBombRange(final int bombRange) {
    this.bombRange = bombRange;
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
