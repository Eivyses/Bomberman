import {MapObject} from './MapObject';
import {Adapter} from "../Adapter";
import {Position, PositionAdapter} from "../Position";

export class Wall extends MapObject {

    constructor(position: Position) {
        super(position);
    }
}

export class WallAdapter implements Adapter<Wall> {
    adapt(item): Wall {
        if (!item) {
            return null;
        }
        const positionAdapter = new PositionAdapter();
        let position = positionAdapter.adapt(item.position)
        return new Wall(position);
    }
}
