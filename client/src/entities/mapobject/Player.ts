import {MapObject} from './MapObject';
import {Adapter} from "../Adapter";
import {Position, PositionAdapter} from "../Position";
import {Direction} from "../Direction";

export class Player extends MapObject {
    public id: string;
    public dead: boolean;
    public killCount: number;
    public stateTime: Float32Array;
    public direction: Direction;


    constructor(position: Position, id: string, dead: boolean, killCount: number, stateTime: Float32Array, direction: Direction) {
        super(position);
        this.id = id;
        this.dead = dead;
        this.killCount = killCount;
        this.stateTime = stateTime;
        this.direction = direction;
    }
}

export class PlayerAdapter implements Adapter<Player> {
    adapt(item): Player {
        if (!item) {
            return null;
        }
        const positionAdapter = new PositionAdapter();
        let position = positionAdapter.adapt(item.position);
        return new Player(position, item.id, item.dead, item.killCount, item.StateTime, item.direction);
    }
}