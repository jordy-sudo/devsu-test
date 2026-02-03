import { Observable } from 'rxjs';
import { Cuenta, CuentaCreate, CuentaUpdate } from '../models/cuenta.model';

export interface CuentaRepository {
  list(): Observable<Cuenta[]>;
  get(id: number): Observable<Cuenta>;
  create(payload: CuentaCreate): Observable<Cuenta>;
  update(id: number, payload: CuentaUpdate): Observable<Cuenta>;
  delete(id: number): Observable<void>;
}
