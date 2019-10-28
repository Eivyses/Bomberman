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
    this.load.spritesheet('bombBoom', 'assets/textures/Bomb_hd_sheet.png', {
      frameWidth: 256,
      frameHeight: 256
    });
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

    //  Show the whole animation sheet
    // this.add.image(32, 32, 'bombBoom', '__BASE').setOrigin(0);

    var config = {
      key: 'walk',
      frames: this.anims.generateFrameNumbers('bombBoom', { start: 0, end: 3 }),
      frameRate: 4,
      yoyo: true,
      repeat: -1
    };

    this.anim = this.anims.create(config);

    this.sprite = this.add
      .sprite(400, 300, 'bombBoom')
      .setOrigin(0, 0)
      .setScale(0.5);
    this.sprite.anims.play('walk');
  }
}
