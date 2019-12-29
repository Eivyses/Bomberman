import 'phaser';
import { TextUtil } from './util/TextUtil';

export class WelcomeScene extends Phaser.Scene {
  private anim: false | Phaser.Animations.Animation;
  private sprite: Phaser.GameObjects.Sprite;

  constructor() {
    super({
      key: 'WelcomeScene'
    });
  }

  preload(): void {
    this.load.image('bomb', 'assets/textures/Bomb_hd.png');
    this.loadSpriteSheets();
  }

  loadSpriteSheets(): void {
    this.load.spritesheet('bombSprite', 'assets/textures/Bomb_hd_sheet.png', {
      frameWidth: 256,
      frameHeight: 256
    });

    // FIXME: use constants
    this.load.spritesheet(
      'playerSprite',
      'assets/textures/bomberman_sprite.png',
      {
        frameWidth: 14,
        frameHeight: 22
      }
    );
  }

  create(): void {
    var titleText: string = 'Bomberman!';
    this.add.text(200, 200, titleText, TextUtil.HEADER_WHITE_TEXT);

    var hintText: string = 'Click to start';
    this.add.text(300, 350, hintText, TextUtil.STANDARD_WHITE_TEXT);

    this.input.on(
      'pointerdown',
      () => {
        this.scene.start('GameScene');
      },
      this
    );

    let currentImage: Phaser.GameObjects.Image = this.add
      .image(300, 200, 'bomb')
      .setOrigin(0, 0);
    currentImage.scale = 0.5;
    // currentImage.displayWidth = 128;
    // currentImage.displayHeight = 128;
  }
}
