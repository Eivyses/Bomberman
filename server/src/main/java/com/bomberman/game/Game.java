package com.bomberman.game;

import com.bomberman.entities.Bomb;
import com.bomberman.entities.BombExplosion;
import com.bomberman.entities.MapObject;
import com.bomberman.entities.Player;
import com.bomberman.entities.Position;
import com.bomberman.entities.Wall;
import com.bomberman.factories.LevelFactory;
import com.bomberman.factories.PlayerFactory;
import com.bomberman.utils.Collisions;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Game {

  private final PlayerFactory playerFactory;
  private final GameState gameState;
  private final LevelFactory levelFactory;

  public Game() {
    playerFactory = new PlayerFactory();
    levelFactory = new LevelFactory();
    gameState = levelFactory.createLevel();
  }

  public String addPlayer(final String id) {
    final var newPlayer = playerFactory.createPlayer(id, gameState);

    gameState.getPlayers().add(newPlayer);

    return newPlayer.getId();
  }

  public void removePlayer(final String id) {
    gameState.getPlayers().removeIf(x -> x.getId().equals(id));
  }

  public void movePlayer(final String playerId, final Position position) {
    final var player =
        gameState.getPlayers().stream().filter(x -> x.getId().equals(playerId)).findFirst();

    if (player.isPresent()) {
      player.get().move(position);
      if (movedOnBomb(player.get())) {
        // TODO: kill
      }
    }
  }

  public boolean movedOnBomb(final Player player) {
    final var explosion =
        gameState.getBombExplosions().stream()
            .filter($explosion -> Collisions.willCollide(player, player.getPosition(), $explosion))
            .findFirst();

    if (explosion.isPresent()) {
      System.out.println(
          String.format(
              "Player %s died from player %s", player.getId(), explosion.get().getPlayerId()));
      return true;
    }
    return false;
  }

  public Optional<Bomb> placeBomb(final String playerId) {
    final var currentTime = ZonedDateTime.now();
    final var player = getPlayer(playerId);
    final var bombDurationInSeconds = player.getBombDurationInSeconds();
    final var position = Position.round(player.getPosition());
    final var explosionTime = currentTime.plusSeconds(bombDurationInSeconds);
    final var bomb = new Bomb(position, explosionTime, player, gameState.getPlayers());
    if (Collisions.willCollide(bomb, position, new ArrayList<>(gameState.getBombs()))) {
      return Optional.empty();
    }
    gameState.getBombs().add(bomb);
    return Optional.of(bomb);
  }

  public void explodeBomb(final Bomb bomb, final String bombId, final String playerId) {
    if (!gameState.getBombs().contains(bomb)) {
      return;
    }
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

    playersInExplosionRange.forEach(
        $player ->
            System.out.println(
                String.format("Player %s died from player %s", $player.getId(), killer.getId())));
  }

  public void removeExplosions(final Bomb bomb) {
    final var explosions =
        gameState.getBombExplosions().stream()
            .filter($explosion -> $explosion.getBombId().equals(bomb.getBombId()))
            .collect(Collectors.toList());

    gameState.getBombExplosions().removeAll(explosions);
  }

  public void initGame() {
    gameState.getWalls().add(new Wall(new Position(32f, 32f)));
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
}
