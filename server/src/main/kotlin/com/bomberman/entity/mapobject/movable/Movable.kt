package com.bomberman.entity.mapobject.movable

import com.bomberman.entity.Position

interface Movable {
    fun move(position: Position);
}