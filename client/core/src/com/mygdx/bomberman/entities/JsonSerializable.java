package com.mygdx.bomberman.entities;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonSerializable {

  public Object toJson() {
    try {
      return new JSONObject(new Gson().toJson(this));
    } catch (final JSONException e) {
      e.printStackTrace();
    }
    return null;
  }
}
