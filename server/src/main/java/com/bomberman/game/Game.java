package com.bomberman.game;

import com.bomberman.entities.Movement;
import com.bomberman.entities.Position;
import com.bomberman.entities.mapobject.Bomb;
import com.bomberman.entities.mapobject.BombExplosion;
import com.bomberman.entities.mapobject.MapObject;
import com.bomberman.entities.mapobject.movable.Player;
import com.bomberman.exception.PlayerNotFoundException;
import com.bomberman.factories.LevelFactory;
import com.bomberman.factories.PlayerFactory;
import com.bomberman.utils.Collisions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Game {

  private final PlayerFactory playerFactory;
  private final GameState gameState;

  public Game() {
    playerFactory = new PlayerFactory();
    final LevelFactory levelFactory = new LevelFactory();
    gameState = levelFactory.createLevel();
  }

  public void addPlayer(final String id) {
    final var newPlayer = playerFactory.createPlayer(id, gameState);

    gameState.getPlayers().add(newPlayer);
  }

  public void removePlayer(final String id) {
    gameState.getPlayers().removeIf(x -> x.getId().equals(id));
  }

  public void movePlayer(final String playerId, final Movement movement) {
    final var player =
        gameState.getPlayers().stream()
            .filter($player -> $player.getId().equals(playerId))
            .findFirst()
            .orElseThrow(() -> new PlayerNotFoundException(playerId));

    if (player.isDead()) {
      return;
    }

    final float movementSpeed = player.getSpeed();
    final var position = movement.getDirection().buildPosition(player.getPosition(), movementSpeed);
    player.move(position);

    player.addStateTime(movement.getDt());

    final var explosion =
        gameState.getBombExplosions().stream()
            .filter($explosion -> Collisions.willCollide(player, player.getPosition(), $explosion))
            .findFirst();

    explosion.ifPresent($explosion -> updateDeathsAndScore($explosion.getPlayerId(), player));
  }

  private void updateDeathsAndScore(final String killerId, final Player player) {
    final var killerOptional =
        gameState.getPlayers().stream()
            .filter($player -> $player.getId().equals(killerId))
            .findFirst();
    killerOptional.ifPresent($killer -> updateDeathsAndScore($killer, player));
  }

  public Optional<Bomb> placeBomb(final String playerId) {
    final var player = getPlayer(playerId);
    if (!player.canPlaceBomb()) {
      return Optional.empty();
    }

    final var position = Position.round(player.getPosition());
    final var bomb =
        new Bomb(position, player.getBombDurationInSeconds(), player, gameState.getPlayers());
    if (Collisions.willCollide(bomb, position, new ArrayList<>(gameState.getBombs()))) {
      return Optional.empty();
    }
    gameState.getBombs().add(bomb);
    player.incrementPlacedBombCount();
    return Optional.of(bomb);
  }

  public void explodeBomb(final Bomb bomb, final String bombId, final String playerId) {
    if (!gameState.getBombs().contains(bomb)) {
      return;
    }
    final var player = getPlayer(playerId);
    player.decrementPlacedBombCount();
    final var bombRange = bomb.getBombRange();
    gameState.getBombs().remove(bomb);
    final List<Bomb> bombsInExplosionRange =
        gameState.getBombs().stream()
            .filter($bomb -> Collisions.isInExplosionRange(bombRange, bomb, $bomb, gameState))
            .collect(Collectors.toList());

    gameState.getBombExplosions().addAll(BombExplosion.of(bomb, bombId, playerId, gameState));
    bombsInExplosionRange.forEach(System.out::println);
    bombsInExplosionRange.forEach($bomb -> explodeBomb($bomb, bombId, playerId));
  }

  public void checkKilled(final Bomb bomb) {
    final List<Player> playersInExplosionRange =
        gameState.getPlayers().stream()
            .filter(
                $player ->
                    Collisions.willCollide(
                        $player,
                        $player.getPosition(),
                        new ArrayList<MapObject>(gameState.getBombExplosions())))
            .collect(Collectors.toList());

    final var killer =
        gameState.getPlayers().stream()
            .filter($player -> $player.getId().equals(bomb.getPlayerId()))
            .findFirst()
            .orElseThrow();

    playersInExplosionRange.forEach($player -> updateDeathsAndScore(killer, $player));
  }

  private void updateDeathsAndScore(final Player killer, final Player killed) {
    if (killed.isDead()) {
      return;
    }

    if (Objects.equals(killer, killed)) {
      killer.killedHimself();
      killer.setDead();
      System.out.println(String.format("Player %s killed himself", killer.getId()));
    } else {
      killer.killedEnemy();
      killed.setDead();
      System.out.println(
          String.format("Player %s died from player %s", killed.getId(), killer.getId()));
    }
  }

  public void removeExplosions(final Bomb bomb) {
    final var explosions =
        gameState.getBombExplosions().stream()
            .filter($explosion -> $explosion.getBombId().equals(bomb.getBombId()))
            .collect(Collectors.toList());

    final var bricksInExplosion =
        gameState.getBricks().stream()
            .filter(
                $brick ->
                    explosions.stream()
                        .anyMatch(
                            $explosion ->
                                Objects.equals($brick.getPosition(), $explosion.getPosition())))
            .collect(Collectors.toList());

    gameState.getBricks().removeAll(bricksInExplosion);
    gameState.getBombExplosions().removeAll(explosions);
  }

  private Player getPlayer(final String playerId) {
    return gameState.getPlayers().stream()
        .filter(x -> x.getId().equals(playerId))
        .findFirst()
        .orElse(null);
  }

  public GameState getGameState() {
    return gameState;
  }

  public void respawnPlayer(final String playerId) {
    final var playerOptional =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    playerOptional.ifPresent(Player::respawn);
  }

  public void increasePlayerSpeed(final String playerId) {
    final var playerOptional =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    playerOptional.ifPresent(Player::increasePlayerSpeed);
  }

  public void decreasePlayerSpeed(final String playerId) {
    final var playerOptional =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    playerOptional.ifPresent(Player::decreasePlayerSpeed);
  }

  public void increaseBombRange(final String playerId) {
    final var playerOptional =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    playerOptional.ifPresent(Player::increaseBombRange);
  }

  public void decreaseBombRange(final String playerId) {
    final var playerOptional =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    playerOptional.ifPresent(Player::decreaseBombRange);
  }

  public void increaseMaxBombCount(final String playerId) {
    final var playerOptional =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    playerOptional.ifPresent(Player::increaseMaxBombCount);
  }
}
