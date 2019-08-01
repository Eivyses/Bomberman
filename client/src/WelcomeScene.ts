import 'phaser';

export class WelcomeScene extends Phaser.Scene {
  constructor() {
    super({
      key: 'WelcomeScene'
    });
  }

  create(): void {
    var titleText: string = 'Bomberman!';
    this.add.text(200, 200, titleText, {
      font: '64px Arial Bold',
      fill: '#FBFBAC'
    });

    var hintText: string = 'Click to start';
    this.add.text(300, 350, hintText, {
      font: '24px Arial Bold',
      fill: '#FBFBAC'
    });

    this.input.on(
      'pointerdown',
      () => {
        this.scene.start('GameScene');
      },
      this
    );
  }
}
