import 'phaser';
import { Game } from 'phaser';
import { GameScene } from './GameScene';
import { WelcomeScene } from './WelcomeScene';

const config: Phaser.Types.Core.GameConfig = {
  type: Phaser.AUTO,
  title: 'Bomberman',
  width: 1920,
  height: 1080,
  scale: {
    mode: Phaser.Scale.FIT,
    autoCenter: Phaser.Scale.CENTER_BOTH
  },
  parent: 'game',
  scene: [WelcomeScene, GameScene],
  backgroundColor: '#18216D'
};
export class BombermanGame extends Phaser.Game {
  constructor(config: Phaser.Types.Core.GameConfig) {
    super(config);
  }
}

window.addEventListener('load', () => {
  var game = new Game(config);
});
