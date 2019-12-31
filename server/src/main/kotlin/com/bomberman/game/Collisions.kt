package com.bomberman.game

import com.bomberman.MAP_HEIGHT
import com.bomberman.MAP_WIDTH
import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.MapObject
import com.bomberman.entity.mapobject.movable.Movable
import com.bomberman.entity.mapobject.pickup.Pickup

fun MapObject.isInExplosionRange(range: Int, toObject: MapObject,
    collideObjects: List<MapObject>): Boolean =
    when {
      this.isInExplosionRange(range, toObject, collideObjects, true, true) -> {
        true
      }
      this.isInExplosionRange(range, toObject, collideObjects, false, true) -> {
        true
      }
      this.isInExplosionRange(range, toObject, collideObjects, true, false) -> {
        true
      }
      this.isInExplosionRange(range, toObject, collideObjects, false, false) -> {
        true
      }
      else -> {
        false
      }
    }

fun MapObject.isInExplosionRange(
    range: Int,
    toObject: MapObject,
    collideObjects: List<MapObject>,
    isDirectionDown: Boolean,
    xAxis: Boolean): Boolean {

  for (i in 1..range) {
    val xPos: Float
    val yPos: Float
    if (xAxis) {
      yPos = this.position.y
      xPos = if (isDirectionDown) {
        this.position.x - i * this.textureWidth
      } else {
        this.position.x + i * this.textureWidth
      }
    } else {
      xPos = this.position.x
      yPos = if (isDirectionDown) {
        this.position.y - i * this.textureHeight
      } else {
        this.position.y + i * this.textureHeight
      }
    }
    val position = Position(xPos, yPos)
    val willHitCollideObject = collideObjects.any {
      it.isAtSameSquare(position)
    }
    if (willHitCollideObject) {
      break
    }
    if (toObject.isAtSameSquare(position)) {
      return true
    }
  }
  return false
}

fun MapObject.isOutOfBound(newPosition: Position): Boolean =
    newPosition.x + this.textureWidth > MAP_WIDTH
        || newPosition.y + this.textureHeight > MAP_HEIGHT
        || newPosition.x < 0
        || newPosition.y < 0

fun MapObject.willCollide(newPosition: Position, obstacles: List<MapObject>): Boolean =
    obstacles.any { this.willCollide(newPosition, it) }

fun <T> T.firstPickupCollision(newPosition: Position,
    pickups: List<Pickup>): Pickup? where T : Movable, T : MapObject =
    pickups.firstOrNull { this.willCollide(newPosition, it) }

fun MapObject.willCollide(
    newPosition: Position, obstacle: MapObject): Boolean {
  if (newPosition == obstacle.position) {
    return true
  }
  val l1 = newPosition.topLeft(this)
  val r1 = newPosition.botRight(this)
  val l2 = obstacle.topLeft
  val r2 = obstacle.botRight
  // If one rectangle is on left side of other
  if (l1.x > r2.x || l2.x > r1.x) {
    return false
  }
  // If one rectangle is above other
  return when {
    l1.y < r2.y || l2.y < r1.y -> {
      false
    }
    else -> true
  }
}

fun MapObject.isAtSameSquare(secondPosition: Position): Boolean =
    this.position == secondPosition