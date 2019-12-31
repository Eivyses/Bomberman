package com.bomberman.entity

import com.bomberman.TEXTURE_SIZE
import com.bomberman.entity.mapobject.MapObject

data class Position(val x: Float, val y: Float) {

  constructor() : this(0f, 0f)

  constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

  fun round(): Position {
    val xPos = (x / TEXTURE_SIZE).toInt() * TEXTURE_SIZE
    val yPos = (y / TEXTURE_SIZE).toInt() * TEXTURE_SIZE
    return Position(xPos, yPos)
  }

  fun topLeft(mapObject: MapObject) =
      Position(x, y + mapObject.textureHeight - 0.01f)

  fun botRight(mapObject: MapObject) =
      Position(x + mapObject.textureWidth - 0.01f, y)
}