// TODO: classes are not used as classes, needs mapping to actual classes :)
export class Position {
    public x: number;
    public y: number;

    public equals(otherPosition: Position): boolean {
        return this.x === otherPosition.x && this.y === otherPosition.y;
    }
}
