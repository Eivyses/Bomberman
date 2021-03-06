import 'phaser';
import {Direction} from './entities/Direction';
import {Drawer} from './game/Drawer';
import {MainGame} from './game/MainGame';
import {SocketClient} from './game/SocketClient';
import {TextUtil} from './util/TextUtil';

export class GameScene extends Phaser.Scene {
    private mainGame: MainGame;
    private socketClient: SocketClient;
    private socketText: Phaser.GameObjects.Text;
    private drawer: Drawer;
    private defaultCursors: Phaser.Types.Input.Keyboard.CursorKeys;
    private wasdCursors: Phaser.Types.Input.Keyboard.CursorKeys;

    constructor() {
        super({
            key: 'GameScene'
        });
    }

    preload(): void {
        this.load.image('bomb', 'assets/textures/Bomb.png');
        this.load.image('brick', 'assets/textures/Brick.png');
        this.load.image('explosion', 'assets/textures/Explosion.png');
        this.load.image('pickup_bomb', 'assets/textures/Pickup_bomb.png');
        this.load.image('pickup_range', 'assets/textures/Pickup_range.png');
        this.load.image('pickup_speed', 'assets/textures/Pickup_speed.png');
        this.load.image('terrain', 'assets/textures/Terrain.png');
        this.load.image('wall', 'assets/textures/Wall.png');

        this.createAnimations();
    }

    createAnimations(): void {
        this.anims.create({
            key: 'bombAnim',
            frames: this.anims.generateFrameNumbers('bombSprite', {
                start: 0,
                end: 3
            }),
            frameRate: 1.3,
            yoyo: false,
            repeat: -1
        });

        this.anims.create({
            key: 'explosionAnim',
            frames: this.anims.generateFrameNumbers('explosionSprite', {
                start: 1,
                end: 32
            }),
            frameRate: 24,
            yoyo: false,
            repeat: 0
        });

        this.createPlayerAnimations();
    }

    private createPlayerAnimations() {
        const keys = ['Down', 'Left', 'Right', 'Up']
        for (let i = 0; i < 4; i++) {

            this.anims.create({
                key: `player${keys[i]}`,
                frames: this.anims.generateFrameNumbers('playerMoveSprite', {
                    start: i * 4,
                    end: i * 4 + 3
                }),
                frameRate: 8,
                yoyo: false,
                repeat: -1
            });
        }
    }

    create(): void {
        this.mainGame = new MainGame();
        this.socketClient = new SocketClient(this.mainGame);
        this.drawer = new Drawer(this);

        this.add.text(200, 200, 'PEW PEW!', TextUtil.HEADER_WHITE_TEXT);

        this.socketText = this.add.text(100, 100, '', TextUtil.HEADER_WHITE_TEXT);
        this.defaultCursors = this.input.keyboard.createCursorKeys();
        this.wasdCursors = this.input.keyboard.addKeys({
            up: Phaser.Input.Keyboard.KeyCodes.W,
            down: Phaser.Input.Keyboard.KeyCodes.S,
            left: Phaser.Input.Keyboard.KeyCodes.A,
            right: Phaser.Input.Keyboard.KeyCodes.D
        });

        this.input.keyboard.on(
            'keydown_R',
            () => {
                this.socketClient.respawnPlayer();
            },
            this
        );

        this.input.keyboard.on(
            'keydown_L',
            () => {
                console.log('explosion');
                console.log(this.drawer.explosionSpritesMap);
                console.log('bomb');
                console.log(this.drawer.bombSpritesMap);
                console.log('brick');
                console.log(this.drawer.brickTextures);
                console.log('pickup');
                console.log(this.drawer.pickupTextures);
                console.log('player');
                console.log(this.drawer.playerSpritesMap);
                console.log('wall');
                console.log(this.drawer.wallTextures);
            },
            this
        );
    }

    update(): void {
        let isMoving: boolean = false;
        if (this.defaultCursors.up.isDown || this.wasdCursors.up.isDown) {
            isMoving = true;
            this.socketClient.move(Direction.UP);
        }
        if (this.defaultCursors.down.isDown || this.wasdCursors.down.isDown) {
            isMoving = true;
            this.socketClient.move(Direction.DOWN);
        }
        if (this.defaultCursors.left.isDown || this.wasdCursors.left.isDown) {
            isMoving = true;
            this.socketClient.move(Direction.LEFT);
        }
        if (this.defaultCursors.right.isDown || this.wasdCursors.right.isDown) {
            isMoving = true;
            this.socketClient.move(Direction.RIGHT);
        }
        if (!isMoving) {
            this.socketClient.move(Direction.IDLE);
        }
        if (this.defaultCursors.space.isDown) {
            this.socketClient.placeBomb();
        }
        if (this.defaultCursors.shift.isDown) {
            this.socketClient.respawnPlayer();
        }

        this.socketText.text = `socket is: ` + this.socketClient.isConnected();
        this.drawer.draw(this.mainGame.gameState);
    }

    destroy(): void {
        this.socketClient.disconnect();
    }
}
