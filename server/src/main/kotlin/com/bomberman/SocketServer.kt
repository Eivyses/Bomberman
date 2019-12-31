package com.bomberman

import com.bomberman.entity.Direction
import com.bomberman.entity.mapobject.Bomb
import com.bomberman.game.Game
import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SocketServer(
    private val game: Game
) {
  private val server: SocketIOServer

  init {
    val config = Configuration()
    config.port = 5050

    server = SocketIOServer(config)

    addEventListeners()

    server.start()
    println("Socket server has started")
  }

  private fun getPlayerId(client: SocketIOClient) =
      client.sessionId.toString()

  private fun addEventListeners() {
    server.addEventListener(
        "connectNewPlayer",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      if (game.playerExists(playerId)) {
        client.sendEvent("connectNewPlayerSuccess", playerId)
        server.broadcastOperations.sendEvent("getGameState", game.gameState)
        return@addEventListener
      }
      println("New player connected: $playerId")
      game.addPlayer(playerId)
      client.sendEvent("connectNewPlayerSuccess", playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }

    server.addEventListener(
        "disconnectPlayer",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.removePlayer(playerId)
      client.sendEvent("disconnectPlayerSuccess")
      println("Player disconnected: $playerId")
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }

    server.addEventListener(
        "movePlayer",
        Direction::class.java
    ) { client, direction, _ ->
      val playerId = getPlayerId(client)
      game.movePlayer(playerId, direction)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }

    server.addEventListener(
        "placeBomb",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      val bomb = game.placeBomb(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
      bomb?.setBombExplosion()
    }

    // DEBUG
    server.addEventListener(
        "respawnPlayer",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.respawnPlayer(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }
    server.addEventListener(
        "increaseBombRange",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.increaseBombRange(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }
    server.addEventListener(
        "decreaseBombRange",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.decreaseBombRange(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }
    server.addEventListener(
        "increasePlayerSpeed",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.increasePlayerSpeed(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }
    server.addEventListener(
        "decreasePlayerSpeed",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.decreasePlayerSpeed(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }
    server.addEventListener(
        "increaseMaxBombCount",
        String::class.java
    ) { client, _, _ ->
      val playerId = getPlayerId(client)
      game.increaseMaxBombCount(playerId)
      server.broadcastOperations.sendEvent("getGameState", game.gameState)
    }

  }

  private fun Bomb.setBombExplosion() {
    val executor = ScheduledThreadPoolExecutor(1)
    executor.schedule(
        {
          game.explodeBomb(this, this.bombId, this.playerId)
          game.checkKilled(this)
          this.removeBombExplosion()
          server.broadcastOperations.sendEvent("getGameState", game.gameState)
        },
        this.explosionTime.toLong(),
        TimeUnit.SECONDS)

  }

  private fun Bomb.removeBombExplosion() {
    val executor = ScheduledThreadPoolExecutor(1)
    executor.schedule(
        {
          game.removeExplosions(this)
          server.broadcastOperations.sendEvent("getGameState", game.gameState)
        },
        1,
        TimeUnit.SECONDS)
  }
}
