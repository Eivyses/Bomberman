import * as io from 'socket.io-client';
import { Configuration } from '../constant/Configuration';
import { Direction } from '../entities/Direction';
import {GameState, GameStateAdapter} from './GameState';
import { MainGame } from './MainGame';

export class SocketClient {
  private socket: SocketIOClient.Socket;
  private mainGame: MainGame;
  private  gameStateAdapter = new GameStateAdapter()

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

  public move(direction: Direction) {
    this.socket.emit('movePlayer', direction);
  }

  placeBomb() {
    this.socket.emit('placeBomb');
  }

  disconnect() {
    this.socket.emit('disconnectPlayer');
  }

  respawnPlayer(): void {
    this.socket.emit('respawnPlayer');
  }

  increaseBombRange(): void {
    this.socket.emit('increaseBombRange');
  }

  decreaseBombRange(): void {
    this.socket.emit('decreaseBombRange');
  }

  increasePlayerSpeed(): void {
    this.socket.emit('increasePlayerSpeed');
  }

  decreasePlayerSpeed(): void {
    this.socket.emit('decreasePlayerSpeed');
  }

  increaseMaxBombCount(): void {
    this.socket.emit('increaseMaxBombCount');
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
      this.mainGame.gameState = this.gameStateAdapter.adapt(gameState);
    });
  }
}
