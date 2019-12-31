package com.bomberman.entity.mapobject

import com.bomberman.entity.Position

abstract class MapObject(
        open var position: Position
) {
    constructor() : this(Position())

    val topLeft: Position
        get() = Position(position.x, position.y + textureHeight - 0.01f)

    val botRight: Position
        get() = Position(position.x + textureWidth - 0.01f, position.y)

    abstract val textureWidth: Int;
    abstract val textureHeight: Int;
}