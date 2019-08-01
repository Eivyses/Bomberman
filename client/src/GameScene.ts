import 'phaser';

export class GameScene extends Phaser.Scene {
  constructor() {
    super({
      key: 'GameScene'
    });
  }

  create(): void {
    var titleText: string = 'PEW PEW!';
    this.add.text(200, 200, titleText, {
      font: '64px Arial Bold',
      fill: '#FBFBAC'
    });
  }
}
