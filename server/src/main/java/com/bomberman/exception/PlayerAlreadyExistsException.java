package com.bomberman.exception;

public class PlayerAlreadyExistsException extends RuntimeException {

  public PlayerAlreadyExistsException() {}

  public PlayerAlreadyExistsException(final String playerId) {
    super(String.format("Player %s is already connected", playerId));
  }
}
