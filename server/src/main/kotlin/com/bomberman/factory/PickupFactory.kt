package com.bomberman.factory

import com.bomberman.entity.mapobject.Brick
import com.bomberman.entity.mapobject.pickup.BombPickup
import com.bomberman.entity.mapobject.pickup.Pickup
import com.bomberman.entity.mapobject.pickup.RangePickup
import com.bomberman.entity.mapobject.pickup.SpeedPickup
import kotlin.random.Random

class PickupFactory {

  private fun createBombPickup(brick: Brick) =
      BombPickup(brick.position)

  private fun createSpeedPickup(brick: Brick) =
      SpeedPickup(brick.position)

  private fun createRangePickup(brick: Brick) =
      RangePickup(brick.position)

  fun createRandomPickup(brick: Brick): Pickup {
    return when (val number = Random.nextInt(0, 3)) {
      0 -> createBombPickup(brick)
      1 -> createSpeedPickup(brick)
      2 -> createRangePickup(brick)
      else -> throw RuntimeException("Pickup number $number is not defined")
    }
  }
}