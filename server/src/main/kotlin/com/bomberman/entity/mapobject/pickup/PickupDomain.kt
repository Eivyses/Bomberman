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

data class SpeedPickup(override var position: Position) : Pickup(position) {
  override fun apply(player: Player) {
    player.increasePlayerSpeed()
  }

  override val className: String
    get() = "pickup_speed"
}

data class RangePickup(override var position: Position) : Pickup(position) {
  override fun apply(player: Player) {
    player.increaseBombRange()
  }

  override val className: String
    get() = "pickup_range"
}