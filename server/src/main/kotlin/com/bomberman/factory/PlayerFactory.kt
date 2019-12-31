package com.bomberman.factory

import com.bomberman.MAP_HEIGHT
import com.bomberman.PLAYER_TEXTURE_HEIGHT
import com.bomberman.entity.Position
import com.bomberman.entity.mapobject.movable.Player
import com.bomberman.game.GameState

class PlayerFactory {

    fun createPlayer(id: String, gameState: GameState): Player =
            Player(
                    id = id,
                    position = Position(0, MAP_HEIGHT - PLAYER_TEXTURE_HEIGHT),
                    gameState = gameState)
}