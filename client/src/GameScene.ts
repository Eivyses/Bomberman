import 'phaser';
import * as io from 'socket.io-client';

export class GameScene extends Phaser.Scene {
  private socket: SocketIOClient.Socket;
  private socketText: Phaser.GameObjects.Text;

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

    this.socketText = this.add.text(100, 100, '', {
      font: '64px Arial Bold',
      fill: `#FBFBAC`
    });

    this.socket = io('http://localhost:5050');

    this.socket.emit('connectNewPlayer');

    this.socket.addEventListener(
      'connectNewPlayerSuccess',
      (playerId: string) => {
        console.log('connected as ' + playerId);
      }
    );
  }

  update(): void {
    this.socketText.text = `socket is: ` + this.socket.connected;
  }
}
