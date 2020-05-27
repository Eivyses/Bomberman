import {MapObject} from './MapObject';
import {Adapter} from "../Adapter";
import {Position, PositionAdapter} from "../Position";

export class BombExplosion extends MapObject {

    constructor(position: Position) {
        super(position);
    }
}

export class BombExplosionAdapter implements Adapter<BombExplosion> {
    adapt(item): BombExplosion {
        if (!item) {
            return null;
        }
        const positionAdapter = new PositionAdapter();
        let position = positionAdapter.adapt(item.position)
        return new BombExplosion(position);
    }
}
