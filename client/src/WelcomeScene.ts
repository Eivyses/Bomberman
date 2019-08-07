import 'phaser';
import { TextUtil } from './util/TextUtil';

export class WelcomeScene extends Phaser.Scene {
  constructor() {
    super({
      key: 'WelcomeScene'
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
  }
}
