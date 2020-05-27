export interface Adapter<T> {
    adapt(item): T;
}