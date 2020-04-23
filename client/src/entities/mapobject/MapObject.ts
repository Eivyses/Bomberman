import {Position} from '../Position';

export abstract class MapObject {
    public position: Position;


    // TODO: create scale function, correctly implement classes (constructor and adaptation)
    public scaledPosition(): Position {
      return new Position();
    }

    public positionEquals(otherPosition: Position): Boolean {
        return this.position.equals(otherPosition);
    }
}
