package com.bomberman.entity

enum class Direction(val value: Int) {
    UP(0), DOWN(1), LEFT(2), RIGHT(3), IDLE(4);

    fun buildPosition(playerPosition: Position, movementSpeed: Float): Position {
        val x: Float
        val y: Float
        when (this) {
            UP -> {
                x = playerPosition.x
                y = playerPosition.y - movementSpeed
            }
            DOWN -> {
                x = playerPosition.x
                y = playerPosition.y + movementSpeed
            }
            LEFT -> {
                x = playerPosition.x - movementSpeed
                y = playerPosition.y
            }
            RIGHT -> {
                x = playerPosition.x + movementSpeed
                y = playerPosition.y
            }
            else -> {
                x = playerPosition.x
                y = playerPosition.y
            }
        }
        return Position(x, y)
    }
}