import { MapObject } from './MapObject';

export class Player extends MapObject {
  public id: string;
  public dead: boolean;
  public killCount: number;
  public stateTime: Float32Array;
}
