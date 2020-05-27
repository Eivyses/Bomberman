import {MapObject} from './MapObject';
import {Adapter} from "../Adapter";
import {Position, PositionAdapter} from "../Position";

export class Pickup extends MapObject {
    public className: string;

    constructor(position: Position, className: string) {
        super(position);
        this.className = className;
    }
}

export class PickupAdapter implements Adapter<Pickup> {
    adapt(item): Pickup {
        if (!item) {
            return null;
        }
        const positionAdapter = new PositionAdapter();
        let position = positionAdapter.adapt(item.position)
        return new Pickup(position, item.className);
    }
}
