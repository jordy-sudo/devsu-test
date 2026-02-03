import { Observable } from 'rxjs';
import { Movimiento, MovimientoCreate } from '../models/movimiento.model';

export interface MovimientoRepository {
  list(): Observable<Movimiento[]>;
  get(id: number): Observable<Movimiento>;
  create(payload: MovimientoCreate): Observable<Movimiento>;
  delete(id: number): Observable<void>;
}
