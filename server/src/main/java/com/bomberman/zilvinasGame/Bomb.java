package com.bomberman.zilvinasGame;

import com.bomberman.entities.Position;
import java.time.ZonedDateTime;

public class Bomb {

    private ZonedDateTime explosionTime;
    private Position position;

    public Bomb(Position position, ZonedDateTime explosionTime) {
        this.position = position;
        this.explosionTime = explosionTime;
    }
}
