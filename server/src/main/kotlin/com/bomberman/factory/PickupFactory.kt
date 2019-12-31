package com.bomberman.factory

import com.bomberman.entity.mapobject.Brick
import com.bomberman.entity.mapobject.pickup.BombPickup

class PickupFactory {

    fun createBombPickup(brick: Brick) =
            BombPickup(brick.position)
}