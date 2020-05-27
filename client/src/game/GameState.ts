import {Pickup, PickupAdapter} from '../entities/mapobject/Pickup';
import {Bomb, BombAdapter} from '../entities/mapobject/Bomb';
import {BombExplosion, BombExplosionAdapter} from '../entities/mapobject/BombExplosion';
import {Brick, BrickAdapter} from '../entities/mapobject/Brick';
import {Player, PlayerAdapter} from '../entities/mapobject/Player';
import {Wall, WallAdapter} from '../entities/mapobject/Wall';
import {Adapter} from "../entities/Adapter";

export class GameState {
    public players: Player[];
    public bombs: Bomb[];
    public walls: Wall[];
    public bombExplosions: BombExplosion[];
    public bricks: Brick[];
    public pickups: Pickup[];

    constructor(players: Player[], bombs: Bomb[], walls: Wall[], bombExplosions: BombExplosion[], bricks: Brick[], pickups: Pickup[]) {
        this.players = players;
        this.bombs = bombs;
        this.walls = walls;
        this.bombExplosions = bombExplosions;
        this.bricks = bricks;
        this.pickups = pickups;
    }
}

export class GameStateAdapter implements Adapter<GameState> {
    adapt(item): GameState {
        if (!item) {
            return null;
        }

        const playerAdapter = new PlayerAdapter()
        const bombAdapter = new BombAdapter()
        const wallAdapter = new WallAdapter()
        const bombExplosionAdapter = new BombExplosionAdapter()
        const brickAdapter = new BrickAdapter()
        const pickupAdapter = new PickupAdapter()

        let players = item.players.map(playerAdapter.adapt)
        let bombs = item.bombs.map(bombAdapter.adapt)
        let walls = item.walls.map(wallAdapter.adapt)
        let bombExplosions = item.bombExplosions.map(bombExplosionAdapter.adapt)
        let bricks = item.bricks.map(brickAdapter.adapt)
        let pickups = item.pickups.map(pickupAdapter.adapt)

        return new GameState(players, bombs, walls, bombExplosions, bricks, pickups);
    }
}
