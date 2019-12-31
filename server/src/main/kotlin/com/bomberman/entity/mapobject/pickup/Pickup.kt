package com.bomberman.entity.mapobject.pickup

import com.bomberman.TEXTURE_SIZE
import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.MapObject
import com.bomberman.entity.mapobject.movable.Player

abstract class Pickup(position: Position) : MapObject(position) {

    abstract fun apply(player: Player)

    abstract val className: String

    override val textureHeight: Int
        get() = TEXTURE_SIZE

    override val textureWidth: Int
        get() = TEXTURE_SIZE
}