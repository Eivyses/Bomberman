import 'phaser';
import { Drawer } from './game/Drawer';
import { MainGame } from './game/MainGame';
import { Server } from './game/Server';
import { TextUtil } from './util/TextUtil';

export class GameScene extends Phaser.Scene {
  private mainGame: MainGame;
  private server: Server;
  private socketText: Phaser.GameObjects.Text;
  private drawer: Drawer;

  constructor() {
    super({
      key: 'GameScene'
    });
  }

  preload(): void {
    this.load.image('bomb', 'assets/textures/Bomb.png');
    this.load.image('player', 'assets/textures/Bomberman.png');
    this.load.image('brick', 'assets/textures/Brick.png');
    this.load.image('explosion', 'assets/textures/Explosion.png');
    this.load.image('pickup_bomb', 'assets/textures/Pickup_bomb.png');
    this.load.image('terrain', 'assets/textures/Terrain.png');
    this.load.image('wall', 'assets/textures/Wall.png');
  }

  create(): void {
    this.mainGame = new MainGame();
    this.server = new Server(this.mainGame);
    this.drawer = new Drawer(this);

    this.add.text(200, 200, 'PEW PEW!', TextUtil.HEADER_WHITE_TEXT);

    this.socketText = this.add.text(100, 100, '', TextUtil.HEADER_WHITE_TEXT);
  }

  update(): void {
    this.socketText.text = `socket is: ` + this.server.isConnected();
    this.drawer.draw(this.mainGame.gameState);
  }
}
