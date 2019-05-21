package com.mygdx.bomberman;

import com.mygdx.bomberman.zilvinasEntities.GameState;
import com.mygdx.bomberman.zilvinasEntities.Player;

public class Game {
    private GameState gameState;
    private String playerId;

    public Game() {
        gameState = new GameState();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Player getCurrentPlayer() {
        return gameState.getPlayers().stream()
                .filter(x -> x.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }
}
