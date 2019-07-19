package com.bomberman.exception;

public class PlayerNotFoundException extends RuntimeException {

  public PlayerNotFoundException() {
  }

  public PlayerNotFoundException(String playerId) {
    super(String.format("Player %s was not found", playerId));
  }
}
