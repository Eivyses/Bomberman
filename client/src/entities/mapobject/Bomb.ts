import {MapObject} from './MapObject';
import {Adapter} from "../Adapter";
import {Position, PositionAdapter} from "../Position";

export class Bomb extends MapObject {
    public bombId: string;

    constructor(position: Position, bombId: string) {
        super(position);
        this.bombId = bombId;
    }
}

export class BombAdapter implements Adapter<Bomb> {
    adapt(item): Bomb {
        if (!item) {
            return null;
        }
        const positionAdapter = new PositionAdapter();
        let position = positionAdapter.adapt(item.position)
        return new Bomb(position, item.bombId);
    }
}