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

    this.load.spritesheet('explosionSprite', 'assets/textures/fancy_bomb_sprite.png', {
      frameWidth: 256,
      frameHeight: 251
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

    this.add.image(300, 200, 'bomb')
      .setOrigin(0, 0)
        .setScale(0.5);

    this.anim = this.anims.create({
      key: 'bombAnim',
      frames: this.anims.generateFrameNumbers('bombSprite', {
        start: 0,
        end: 32
      }),
      frameRate: 10,
      yoyo: false,
      repeat: -1
    });
    this.sprite = this.add.sprite(400, 400, 'bombSprite')
        .setOrigin(0,0)
        .setScale(1);
    this.sprite.play('bombAnim');
  }
}
