import { Pickup } from '../entities/mapobject/pickup/Pickup';
import { Bomb } from './../entities/mapobject/Bomb';
import { BombExplosion } from './../entities/mapobject/BombExplosion';
import { Brick } from './../entities/mapobject/Brick';
import { Player } from './../entities/mapobject/Player';
import { Wall } from './../entities/mapobject/Wall';

export class GameState {
  public players: Player[];
  public bombs: Bomb[];
  public walls: Wall[];
  public bombExplosions: BombExplosion[];
  public bricks: Brick[];
  public pickups: Pickup[];

  constructor() {
    this.players = [];
    this.bombs = [];
    this.walls = [];
    this.bombExplosions = [];
    this.bricks = [];
    this.pickups = [];
  }
}
