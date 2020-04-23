export class Position {
    public x: number;
    public y: number;

    public equals(otherPosition: Position): boolean {
        return this.x === otherPosition.x && this.y === otherPosition.y;
    }
}
