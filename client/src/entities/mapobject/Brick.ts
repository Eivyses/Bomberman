import {MapObject} from './MapObject';
import {Adapter} from "../Adapter";
import {Position, PositionAdapter} from "../Position";

export class Brick extends MapObject {

    constructor(position: Position) {
        super(position);
    }
}

export class BrickAdapter implements Adapter<Brick> {
    adapt(item): Brick {
        if (!item) {
            return null;
        }
        const positionAdapter = new PositionAdapter();
        let position = positionAdapter.adapt(item.position)
        return new Brick(position);
    }
}