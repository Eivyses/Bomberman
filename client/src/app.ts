import 'phaser';
import { Game } from 'phaser';
import { WelcomeScene } from './WelcomeScene';
import { GameScene } from './GameScene';

const config: Phaser.Types.Core.GameConfig = {
  title: 'Bomberman',
  width: 800,
  height: 600,
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
