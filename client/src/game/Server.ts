import * as io from 'socket.io-client';
import { Configuration } from '../constant/Configuration';
import { GameState } from './GameState';
import { MainGame } from './MainGame';

export class Server {
  private socket: SocketIOClient.Socket;
  private mainGame: MainGame;

  constructor(mainGame: MainGame) {
    this.mainGame = mainGame;

    this.connectSocket();
    this.configureListeners();
  }

  public isConnected(): boolean {
    return this.socket.connected;
  }

  private connectSocket(): void {
    this.socket = io(
      Configuration.SERVER_ADDRESS + ':' + Configuration.SERVER_PORT
    );
    this.socket.connect();
  }

  private configureListeners(): void {
    this.socket.addEventListener('connect', () => {
      this.socket.emit('connectNewPlayer');
      console.log('connect');
    });

    this.socket.addEventListener(
      'connectNewPlayerSuccess',
      (playerId: string) => {
        console.log('connected as ' + playerId);
      }
    );

    this.socket.addEventListener('disconnect', () => {
      console.log('disconnected ');
    });

    this.socket.addEventListener('getGameState', (gameState: GameState) => {
      console.log(gameState);
      this.mainGame.gameState = gameState;
    });
  }
}
