package com.bomberman

class PlayerAlreadyExistsException(playerId: String) :
        RuntimeException("Player $playerId is already connected")

class PlayerNotFoundException(playerId: String) :
        RuntimeException("Player $playerId was not found")