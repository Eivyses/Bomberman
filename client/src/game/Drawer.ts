import {MapObject} from '../entities/mapobject/MapObject';
import {Pickup} from '../entities/mapobject/Pickup';
import {Player} from '../entities/mapobject/Player';
import {Wall} from '../entities/mapobject/Wall';
import {Bomb} from '../entities/mapobject/Bomb';
import {BombExplosion} from '../entities/mapobject/BombExplosion';
import {Brick} from '../entities/mapobject/Brick';
import {GameState} from './GameState';
import {Configuration} from "../constant/Configuration";
import {Direction} from "../entities/Direction";
import Image = Phaser.GameObjects.Image;
import Sprite = Phaser.GameObjects.Sprite;

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
        bombSprite = this.createAndScaleSprite(bomb, 'bombSprite', 0.18);
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
        let explosionSprite = this.explosionSpritesMap.get(bombExplosion.position.asString());
        if (explosionSprite) {
            return;
        }
        explosionSprite = this.createAndScaleSprite(bombExplosion, 'explosionSprite', 0.125);
        this.explosionSpritesMap.set(bombExplosion.position.asString(), explosionSprite);
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
            this.moveSprite(playerSprite, player.direction);
            playerSprite.setPosition(player.position.x * Configuration.SCALE_FACTOR, player.position.y * Configuration.SCALE_FACTOR);
            return;
        }
        // TODO: scaling is a bit off
        playerSprite = this.createAndScaleSprite(player, 'playerMoveSprite', 0.25);
        this.playerSpritesMap.set(player.id, playerSprite);
        this.moveSprite(playerSprite, player.direction);
    }

    moveSprite(sprite: Sprite, direction: Direction) {
        if (direction === Direction.UP) {
            sprite.play('playerUp', true)
        }
        if (direction === Direction.DOWN) {
            sprite.play('playerDown', true)
        }
        if (direction === Direction.LEFT) {
            sprite.play('playerLeft', true)
        }
        if (direction === Direction.RIGHT) {
            sprite.play('playerRight', true)
        }
        if (direction === Direction.IDLE) {
            sprite.anims.stop()
        }
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

        this.destroyPlayers(gameState.players);
        this.destroySprites(gameState.bombs, this.bombSpritesMap, ((mapObject: Bomb) => mapObject.bombId))
        this.destroySprites(gameState.bombExplosions, this.explosionSpritesMap);
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

    private defaultConvertFun = function (mapObject: MapObject) {
        return mapObject.position.asString()
    }

    destroySprites(mapObjects: MapObject[],
                   spriteMap: Map<string, Sprite>,
                   convertFunction: (mapObject: MapObject) => string = this.defaultConvertFun) {
        let positions = mapObjects.map(convertFunction);
        spriteMap.forEach((value, key) => {
            if (!positions.includes(key)) {
                spriteMap.get(key).destroy();
                spriteMap.delete(key);
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
}
