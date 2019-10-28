import { Pickup } from '../entities/mapobject/pickup/Pickup';
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
  private pickupTextures: Phaser.GameObjects.Image[] = [];

  private bombSprites: Phaser.GameObjects.Sprite[] = [];

  private frame: number = 0;

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

    this.destroyOldGraphics(gameState);
    this.drawBricks(gameState.bricks);
    this.drawWalls(gameState.walls);
    this.drawBombs(gameState.bombs);
    this.drawExplosions(gameState.bombExplosions);
    this.drawPickups(gameState.pickups);

    this.drawPlayers(gameState.players);
  }

  drawPickups(pickups: Pickup[]): void {
    pickups.forEach(pickup => this.drawPickup(pickup));
  }

  drawPickup(pickup: Pickup): void {
    this.pickupTextures.push(
      this.game.add
        .image(pickup.position.x, pickup.position.y, pickup.className)
        .setOrigin(0, 0)
    );
  }

  drawBricks(bricks: Brick[]): void {
    bricks.forEach(brick => this.drawBrick(brick));
  }
  drawBrick(brick: Brick): void {
    this.brickTextures.push(
      this.game.add
        .image(brick.position.x, brick.position.y, 'brick')
        .setOrigin(0, 0)
    );
  }

  drawBombs(bombs: Bomb[]): void {
    bombs.forEach(bomb => this.drawBomb(bomb));
  }

  drawBomb(bomb: Bomb): void {
    if (this.bombExists(bomb)) {
      return;
    }
    let bombSprite = this.game.add
      .sprite(bomb.position.x, bomb.position.y, 'bombBoom')
      .setOrigin(0, 0)
      .setScale(0.125);
    this.bombSprites.push(bombSprite);
    bombSprite.play('bombTick');
  }

  bombExists(bomb: Bomb): boolean {
    for (let i = 0; i < this.bombSprites.length; i++) {
      const pos = this.bombSprites[i].getTopLeft();
      if (pos.x === bomb.position.x && pos.y === bomb.position.y) {
        return true;
      }
    }
    return false;
  }

  drawExplosions(bombExplosions: BombExplosion[]): void {
    bombExplosions.forEach(bombExplosion => this.drawExplosion(bombExplosion));
  }
  drawExplosion(bombExplosion: BombExplosion): void {
    this.explosionTextures.push(
      this.game.add
        .image(bombExplosion.position.x, bombExplosion.position.y, 'explosion')
        .setOrigin(0, 0)
    );
  }

  drawPlayers(players: Player[]): void {
    players.forEach(player => this.drawPlayer(player));
  }
  drawPlayer(player: Player): void {
    const texture: string = player.dead ? 'explosion' : 'player';
    this.playerTextures.push(
      this.game.add
        .image(player.position.x, player.position.y, texture)
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

  destroyOldGraphics(gameState: GameState) {
    []
      .concat(
        this.wallTextures,
        this.bombTextures,
        this.playerTextures,
        this.brickTextures,
        this.explosionTextures,
        this.pickupTextures
      )
      .forEach((texture: Phaser.GameObjects.Image) => texture.destroy());

    // TODO: prettify
    let toDelete: Phaser.GameObjects.Sprite[] = [];
    this.bombSprites.forEach(bombSprite => {
      if (!this.bombSpriteExists(bombSprite, gameState.bombs)) {
        bombSprite.destroy();
        toDelete.push(bombSprite);
      }
    });
    toDelete.forEach(item => {
      const pos = this.bombSprites.indexOf(item);
      this.bombSprites.splice(pos, 1);
    });
    console.log(this.bombSprites);
  }

  // TODO: prettify
  bombSpriteExists(
    bombSprite: Phaser.GameObjects.Sprite,
    bombs: Bomb[]
  ): boolean {
    for (let i = 0; i < bombs.length; i++) {
      const pos = bombs[i].position;
      const spriteBomb = bombSprite.getTopLeft();
      if (pos.x === spriteBomb.x && pos.y === spriteBomb.y) {
        return true;
      }
    }
    return false;
  }
}
