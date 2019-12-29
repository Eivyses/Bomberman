import { MapObject } from '../entities/mapobject/MapObject';
import { Pickup } from '../entities/mapobject/pickup/Pickup';
import { Player } from '../entities/mapobject/Player';
import { Wall } from '../entities/mapobject/Wall';
import { Bomb } from './../entities/mapobject/Bomb';
import { BombExplosion } from './../entities/mapobject/BombExplosion';
import { Brick } from './../entities/mapobject/Brick';
import { GameState } from './GameState';

export class Drawer {
  private game: Phaser.Scene;

  wallTextures: Phaser.GameObjects.Image[] = [];
  brickTextures: Phaser.GameObjects.Image[] = [];
  explosionTextures: Phaser.GameObjects.Image[] = [];
  terrainTexture: Phaser.GameObjects.Image;
  pickupTextures: Phaser.GameObjects.Image[] = [];

  bombSprites: Phaser.GameObjects.Sprite[] = [];
  // playerSprites: Phaser.GameObjects.Sprite[] = [];

  playerSpritesMap: Map<string, Phaser.GameObjects.Sprite> = new Map();

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
    if (this.imageExists(pickup, this.pickupTextures)) {
      return;
    }

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
    if (this.imageExists(brick, this.brickTextures)) {
      return;
    }

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
    if (this.spriteExists(bomb, this.bombSprites)) {
      return;
    }
    let bombSprite = this.game.add
      .sprite(bomb.position.x, bomb.position.y, 'bombSprite')
      .setOrigin(0, 0)
      .setScale(0.125);
    this.bombSprites.push(bombSprite);
    bombSprite.play('bombAnim');
  }

  spriteExists(
    mapObject: MapObject,
    sprites: Phaser.GameObjects.Sprite[]
  ): boolean {
    let answer: boolean = false;
    sprites
      .map(sprite => sprite.getTopLeft())
      .forEach(pos => {
        if (pos.x === mapObject.position.x && pos.y === mapObject.position.y) {
          answer = true;
        }
      });
    return answer;
  }

  imageExists(
    mapObject: MapObject,
    textures: Phaser.GameObjects.Image[]
  ): boolean {
    let answer: boolean = false;
    textures
      .map(texture => texture.getTopLeft())
      .forEach(pos => {
        if (pos.x === mapObject.position.x && pos.y === mapObject.position.y) {
          answer = true;
        }
      });
    return answer;
  }

  drawExplosions(bombExplosions: BombExplosion[]): void {
    bombExplosions.forEach(bombExplosion => this.drawExplosion(bombExplosion));
  }
  drawExplosion(bombExplosion: BombExplosion): void {
    if (this.imageExists(bombExplosion, this.explosionTextures)) {
      return;
    }

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
    // FIXME: get a death sprite/animation
    if (player.dead) {
      return;
    }
    let playerSprite = this.playerSpritesMap[player.id];
    if (playerSprite) {
      playerSprite.setPosition(player.position.x, player.position.y);
      return;
    }
    // TODO: good luck with diagonal animations
    playerSprite = this.game.add
      .sprite(player.position.x, player.position.y, 'playerSprite')
      .setOrigin(0, 0);
    this.playerSpritesMap[player.id] = playerSprite;
    // this.playerSprites.push(playerSprite);
    playerSprite.play('playerAnim');
  }

  drawWalls(walls: Wall[]): void {
    walls.forEach(wall => this.drawWall(wall));
  }

  drawWall(wall: Wall): void {
    if (this.imageExists(wall, this.wallTextures)) {
      return;
    }

    this.wallTextures.push(
      this.game.add
        .image(wall.position.x, wall.position.y, 'wall')
        .setOrigin(0, 0)
    );
  }

  destroyOldGraphics(gameState: GameState) {
    this.destroyOldImages(gameState.walls, this.wallTextures);
    this.destroyOldImages(gameState.bricks, this.brickTextures);
    this.destroyOldImages(gameState.bombExplosions, this.explosionTextures);
    this.destroyOldImages(gameState.pickups, this.pickupTextures);

    this.destroyOldSprites(gameState.bombs, this.bombSprites);

    this.destroyPlayers(gameState.players);
  }

  destroyOldImages(
    mapObjects: MapObject[],
    textures: Phaser.GameObjects.Image[]
  ) {
    let toDelete: Phaser.GameObjects.Image[] = [];
    textures.forEach(texture => {
      if (!this.mapObjectExists(texture, mapObjects)) {
        texture.destroy();
        toDelete.push(texture);
      }
    });
    toDelete.forEach(item => {
      const pos = textures.indexOf(item);
      textures.splice(pos, 1);
    });
  }

  destroyOldSprites(
    mapObjects: MapObject[],
    textures: Phaser.GameObjects.Sprite[]
  ) {
    let toDelete: Phaser.GameObjects.Sprite[] = [];
    textures.forEach(texture => {
      if (!this.mapObjectExists(texture, mapObjects)) {
        texture.destroy();
        toDelete.push(texture);
      }
    });
    toDelete.forEach(item => {
      const pos = textures.indexOf(item);
      textures.splice(pos, 1);
    });
  }

  destroyPlayers(players: Player[]) {
    players
      .filter(player => player.dead)
      .map(player => player.id)
      .filter(playerId => this.playerSpritesMap[playerId])
      .forEach(playerId => {
        console.log('player destroyed');
        this.playerSpritesMap[playerId].destroy();
        this.playerSpritesMap[playerId] = undefined;
      });
  }

  // TODO: prettify
  mapObjectExists(
    texture: Phaser.GameObjects.Sprite | Phaser.GameObjects.Image,
    mapObjects: MapObject[]
  ): boolean {
    const spritePos = texture.getTopLeft();
    let answer: boolean = false;
    mapObjects
      .map(bomb => bomb.position)
      .forEach(pos => {
        if (pos.x === spritePos.x && pos.y === spritePos.y) {
          answer = true;
        }
      });
    return answer;
  }
}
