package com.mygdx.bomberman.dto;

import java.util.HashMap;
import java.util.Map;

public class PlayersDto {
    private Map<String, PlayerDto> players;

    public PlayersDto(){
        players = new HashMap<>();
    }

    public Map<String, PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, PlayerDto> players) {
        this.players = players;
    }
}
