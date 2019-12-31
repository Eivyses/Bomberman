package com.bomberman.game

import com.bomberman.entity.mapobject.Bomb
import com.bomberman.entity.mapobject.BombExplosion
import com.bomberman.entity.mapobject.Brick
import com.bomberman.entity.mapobject.MapObject
import com.bomberman.entity.mapobject.Wall
import com.bomberman.entity.mapobject.movable.Player
import com.bomberman.entity.mapobject.pickup.Pickup


// TODO: stackoverflow on toString
data class GameState(
    val players: MutableList<Player> = mutableListOf(),
    val bombs: MutableList<Bomb> = mutableListOf(),
    val walls: MutableList<Wall> = mutableListOf(),
    val bombExplosions: MutableList<BombExplosion> = mutableListOf(),
    val bricks: MutableList<Brick> = mutableListOf(),
    val pickups: MutableList<Pickup> = mutableListOf()
) {
  val obstacles: List<MapObject>
    get() =
      listOf(walls, bricks).flatten()

  val collideObjects: List<MapObject>
    get() =
      listOf(walls).flatten()
}