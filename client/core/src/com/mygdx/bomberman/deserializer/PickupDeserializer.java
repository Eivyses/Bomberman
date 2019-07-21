package com.mygdx.bomberman.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mygdx.bomberman.entities.mapobject.pickup.BombPickup;
import com.mygdx.bomberman.entities.mapobject.pickup.Pickup;
import java.lang.reflect.Type;

public class PickupDeserializer implements JsonDeserializer<Pickup> {

  private static final String CLASS_TYPE = "className";

  @Override
  public Pickup deserialize(
      final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
      throws JsonParseException {
    final String className = json.getAsJsonObject().get(CLASS_TYPE).getAsString();
    switch (className) {
      case "BombPickup":
        return context.deserialize(json, BombPickup.class);
      default:
        return null;
    }
  }
}
