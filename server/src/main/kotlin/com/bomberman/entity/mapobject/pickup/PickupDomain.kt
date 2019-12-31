package com.bomberman.entity.mapobject.pickup

import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.movable.Player

data class BombPickup(override var position: Position) : Pickup(position) {
    override fun apply(player: Player) {
        player.increaseMaxBombCount()
    }

    override val className: String
        get() = "pickup_bomb"
}