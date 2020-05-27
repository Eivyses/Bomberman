import {Adapter} from "./Adapter";

export class Position {
    public x: number;
    public y: number;

    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }

    public asString(): string {
        return `${this.x}:${this.y}`
    }

    public equals(otherPosition: Position): boolean {
        return this.x === otherPosition.x && this.y === otherPosition.y;
    }
}

export class PositionAdapter implements Adapter<Position> {
    adapt(item): Position {
        if (!item) {
            return null;
        }
        return new Position(item.x,item.y);
    }
}