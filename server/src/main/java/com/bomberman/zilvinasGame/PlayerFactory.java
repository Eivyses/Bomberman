package com.bomberman.zilvinasGame;

import com.bomberman.entities.Position;

public class PlayerFactory {

    public Player createPlayer(String id) {
        return new Player(id, new Position(0, 0));
    }
}
