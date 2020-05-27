import {MapObject} from '../entities/mapobject/MapObject';
import {Pickup} from '../entities/mapobject/pickup/Pickup';
import {Player} from '../entities/mapobject/Player';
import {Wall} from '../entities/mapobject/Wall';
import {Bomb} from '../entities/mapobject/Bomb';
import {BombExplosion} from '../entities/mapobject/BombExplosion';
import {Brick} from '../entities/mapobject/Brick';
import {GameState} from './GameState';
import {Configuration} from "../constant/Configuration";
import Image = Phaser.GameObjects.Image;
import Sprite = Phaser.GameObjects.Sprite;
import {Position} from "../entities/Position";

export class Drawer {
    private game: Phaser.Scene;

    wallTextures: Phaser.GameObjects.Image[] = [];
    brickTextures: Phaser.GameObjects.Image[] = [];
    terrainTexture: Phaser.GameObjects.Image;
    pickupTextures: Phaser.GameObjects.Image[] = [];

    playerSpritesMap: Map<string, Phaser.GameObjects.Sprite> = new Map();
    bombSpritesMap: Map<string, Phaser.GameObjects.Sprite> = new Map();
    explosionSpritesMap: Map<string, Phaser.GameObjects.Sprite> = new Map();

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

        this.pickupTextures.push(this.createAndScaleImage(pickup, pickup.className));
    }

    private createAndScaleImage(mapObject: MapObject, textureName: string): Image {
        return this.game.add.image(mapObject.position.x * Configuration.SCALE_FACTOR, mapObject.position.y * Configuration.SCALE_FACTOR, textureName)
            .setOrigin(0, 0)
            .setScale(Configuration.SCALE_FACTOR)
    }

    drawBricks(bricks: Brick[]): void {
        bricks.forEach(brick => this.drawBrick(brick));
    }

    drawBrick(brick: Brick): void {
        if (this.imageExists(brick, this.brickTextures)) {
            return;
        }

        this.brickTextures.push(this.createAndScaleImage(brick, 'brick'));
    }

    drawBombs(bombs: Bomb[]): void {
        bombs.forEach(bomb => this.drawBomb(bomb));
    }

    drawBomb(bomb: Bomb): void {
        let bombSprite = this.bombSpritesMap.get(bomb.bombId);
        if (bombSprite) {
            bombSprite.setPosition(bomb.position.x * Configuration.SCALE_FACTOR, bomb.position.y * Configuration.SCALE_FACTOR);
            return;
        }
        bombSprite = this.createAndScaleSprite(bomb, 'bombSprite', 0.125);
        this.bombSpritesMap.set(bomb.bombId, bombSprite);
        bombSprite.play('bombAnim');
    }

    private createAndScaleSprite(mapObject: MapObject, textureName: string, scale: number = 1): Sprite {
        return this.game.add.sprite(mapObject.position.x * Configuration.SCALE_FACTOR, mapObject.position.y * Configuration.SCALE_FACTOR, textureName)
            .setOrigin(0, 0)
            .setScale(scale * Configuration.SCALE_FACTOR)
    }

    imageExists(
        mapObject: MapObject,
        textures: Phaser.GameObjects.Image[]
    ): boolean {
        let answer: boolean = false;
        textures
            .map(texture => texture.getTopLeft())
            .forEach(pos => {
                if (pos.x === mapObject.position.x * Configuration.SCALE_FACTOR && pos.y === mapObject.position.y * Configuration.SCALE_FACTOR) {
                    answer = true;
                }
            });
        return answer;
    }

    drawExplosions(bombExplosions: BombExplosion[]): void {
        bombExplosions.forEach(bombExplosion => this.drawExplosion(bombExplosion));
    }

    drawExplosion(bombExplosion: BombExplosion): void {
        let explosionSprite = this.explosionSpritesMap.get(this.asMapKey(bombExplosion.position));
        if (explosionSprite) {
            return;
        }
        explosionSprite = this.createAndScaleSprite(bombExplosion, 'explosionSprite', 0.125);
        this.explosionSpritesMap.set(this.asMapKey(bombExplosion.position), explosionSprite);
        explosionSprite.play('explosionAnim');
    }

    drawPlayers(players: Player[]): void {
        players.forEach(player => this.drawPlayer(player));
    }

    drawPlayer(player: Player): void {
        // FIXME: get a death sprite/animation
        if (player.dead) {
            return;
        }
        let playerSprite = this.playerSpritesMap.get(player.id);
        if (playerSprite) {
            playerSprite.setPosition(player.position.x * Configuration.SCALE_FACTOR, player.position.y * Configuration.SCALE_FACTOR);
            return;
        }
        // TODO: good luck with movement animations
        playerSprite = this.createAndScaleSprite(player, 'playerSprite');
        this.playerSpritesMap.set(player.id, playerSprite);
        playerSprite.play('playerAnim');
    }

    drawWalls(walls: Wall[]): void {
        walls.forEach(wall => this.drawWall(wall));
    }

    drawWall(wall: Wall): void {
        if (this.imageExists(wall, this.wallTextures)) {
            return;
        }

        this.wallTextures.push(this.createAndScaleImage(wall, 'wall'));
    }

    destroyOldGraphics(gameState: GameState) {
        this.destroyOldImages(gameState.walls, this.wallTextures);
        this.destroyOldImages(gameState.bricks, this.brickTextures);
        this.destroyOldImages(gameState.pickups, this.pickupTextures);

        this.destroyBombs(gameState.bombs);
        this.destroyPlayers(gameState.players);
        this.destroyExplosions(gameState.bombExplosions);
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

    destroyBombs(bombs: Bomb[]) {
        let bombIds = bombs.map(bomb => bomb.bombId);
        this.bombSpritesMap.forEach((value, key) => {
            if (!bombIds.includes(key)) {
                this.bombSpritesMap.get(key).destroy();
                this.bombSpritesMap.delete(key);
            }
        });
    }

    // TODO: rework to be generic
    destroyExplosions(explosions: BombExplosion[]) {
        let positions = explosions.map(explosion => this.asMapKey(explosion.position));
        this.explosionSpritesMap.forEach((value, key) => {
            if (!positions.includes(key)) {
                this.explosionSpritesMap.get(key).destroy();
                this.explosionSpritesMap.delete(key);
            }
        });
    }

    destroyPlayers(players: Player[]) {
        players
            .filter(player => player.dead)
            .map(player => player.id)
            .filter(playerId => this.playerSpritesMap.has(playerId))
            .forEach(playerId => {
                console.log('player destroyed');
                this.playerSpritesMap.get(playerId).destroy();
                this.playerSpritesMap.delete(playerId);
            });
    }

    mapObjectExists(
        texture: Phaser.GameObjects.Sprite | Phaser.GameObjects.Image,
        mapObjects: MapObject[]
    ): boolean {
        const spritePos = texture.getTopLeft();
        let answer: boolean = false;
        mapObjects
            .map(mapObject => mapObject.position)
            .forEach(pos => {
                if (pos.x * Configuration.SCALE_FACTOR === spritePos.x && pos.y * Configuration.SCALE_FACTOR === spritePos.y) {
                    answer = true;
                }
            });
        return answer;
    }

    private asMapKey(position: Position): string {
        return `${position.x}:${position.y}`;
    }
}
