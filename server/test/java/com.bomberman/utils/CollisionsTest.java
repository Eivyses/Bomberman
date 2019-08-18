package com.bomberman.utils;

import com.bomberman.TestDataGenerator;
import com.bomberman.entities.mapobject.movable.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CollisionsTest {

  @Test
  void willCollide_playerAboveObstacle_moveDown() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 6);

    final Player player = generator.getPlayer("player1");

    Assertions.assertTrue(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(7, 6),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerAboveObstacle_moveUp() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 6);

    final Player player = generator.getPlayer("player1");

    Assertions.assertFalse(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(7, 4),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerAboveObstacle_moveLeft() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 6);

    final Player player = generator.getPlayer("player1");

    Assertions.assertFalse(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(6, 5),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerAboveObstacle_moveRight() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 6);

    final Player player = generator.getPlayer("player1");

    Assertions.assertFalse(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(8, 5),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerUnderObstacle_moveDown() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 4);

    final Player player = generator.getPlayer("player1");

    Assertions.assertFalse(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(7, 6),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerUnderObstacle_moveUp() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 4);

    final Player player = generator.getPlayer("player1");

    Assertions.assertTrue(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(7, 4),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerUnderObstacle_moveLeft() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 4);

    final Player player = generator.getPlayer("player1");

    Assertions.assertFalse(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(6, 5),
            generator.getGameState().getObstacles()));
  }

  @Test
  void willCollide_playerUnderObstacle_moveRight() {
    final TestDataGenerator generator =
        TestDataGenerator.newGenerator()
            .createPlayerAtColRow("player1", 7, 5)
            .createWallAtColRow(7, 4);

    final Player player = generator.getPlayer("player1");

    Assertions.assertFalse(
        Collisions.willCollide(
            player,
            TestDataGenerator.positionAtColRow(8, 5),
            generator.getGameState().getObstacles()));
  }
}
