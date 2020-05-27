import {Position} from "../Position";

export abstract class MapObject {
    public position: Position;

    protected constructor(position: Position) {
        this.position = position;
    }

    // TODO: create scale function
    public scaledPosition(): Position {
        return new Position(0, 0);
    }

    public positionEquals(otherPosition: Position): Boolean {
        return this.position.equals(otherPosition);
    }
}
