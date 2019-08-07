import { Player } from '../entities/mapobject/Player';
import { Wall } from '../entities/mapobject/Wall';
import { Bomb } from './../entities/mapobject/Bomb';
import { BombExplosion } from './../entities/mapobject/BombExplosion';
import { Brick } from './../entities/mapobject/Brick';
import { GameState } from './GameState';

export class Drawer {
  private game: Phaser.Scene;

  private wallTextures: Phaser.GameObjects.Image[] = [];
  private bombTextures: Phaser.GameObjects.Image[] = [];
  private playerTextures: Phaser.GameObjects.Image[] = [];
  private brickTextures: Phaser.GameObjects.Image[] = [];
  private explosionTextures: Phaser.GameObjects.Image[] = [];
  private terrainTexture: Phaser.GameObjects.Image;
  private pickupBombTextures: Phaser.GameObjects.Image[] = [];

  constructor(game: Phaser.Scene) {
    this.game = game;
    this.loadStaticGraphics();
  }

  loadStaticGraphics(): void {
    this.terrainTexture = this.game.add.image(0, 0, 'terrain').setOrigin(0, 0);
    this.terrainTexture.displayHeight = this.game.scale.height;
    this.terrainTexture.displayWidth = this.game.scale.width;
    console.log(this.game.scale.width);
    console.log(this.game.scale.height);
  }

  draw(gameState: GameState): void {
    if (!gameState) {
      return;
    }

    this.destroyOldGraphics();
    this.drawBricks(gameState.bricks);
    this.drawWalls(gameState.walls);
    this.drawBombs(gameState.bombs);
    this.drawExplosions(gameState.bombExplosions);

    this.drawPlayers(gameState.players);
  }

  drawBricks(bricks: Brick[]): void {
    bricks.forEach(bricks => this.drawBrick(bricks));
  }
  drawBrick(brick: Brick): void {
    this.brickTextures.push(
      this.game.add
        .image(brick.position.x, brick.position.y, 'brick')
        .setOrigin(0, 0)
    );
  }

  drawBombs(bombs: Bomb[]): void {
    bombs.forEach(bombs => this.drawBomb(bombs));
  }
  drawBomb(bomb: Bomb): void {
    this.bombTextures.push(
      this.game.add
        .image(bomb.position.x, bomb.position.y, 'bomb')
        .setOrigin(0, 0)
    );
  }

  drawExplosions(bombExplosions: BombExplosion[]): void {
    bombExplosions.forEach(bombExplosions =>
      this.drawExplosion(bombExplosions)
    );
  }
  drawExplosion(bombExplosion: BombExplosion): void {
    this.explosionTextures.push(
      this.game.add
        .image(bombExplosion.position.x, bombExplosion.position.y, 'explosion')
        .setOrigin(0, 0)
    );
  }

  drawPlayers(players: Player[]): void {
    players.forEach(players => this.drawPlayer(players));
  }
  drawPlayer(player: Player): void {
    this.playerTextures.push(
      this.game.add
        .image(player.position.x, player.position.y, 'player')
        .setOrigin(0, 0)
    );
  }

  drawWalls(walls: Wall[]): void {
    walls.forEach(wall => this.drawWall(wall));
  }

  drawWall(wall: Wall): void {
    this.wallTextures.push(
      this.game.add
        .image(wall.position.x, wall.position.y, 'wall')
        .setOrigin(0, 0)
    );
  }

  destroyOldGraphics() {
    []
      .concat(
        this.wallTextures,
        this.bombTextures,
        this.playerTextures,
        this.brickTextures,
        this.explosionTextures,
        this.pickupBombTextures
      )
      .forEach((texture: Phaser.GameObjects.Image) => texture.destroy());
  }
}
