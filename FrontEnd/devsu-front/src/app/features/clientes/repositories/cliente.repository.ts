import { Observable } from 'rxjs';
import { Cliente } from '../models/cliente.model';

export interface ClienteRepository {
  list(): Observable<Cliente[]>;
  get(id: number): Observable<Cliente>;
  create(payload: Partial<Cliente>): Observable<Cliente>;
  update(id: number, payload: Partial<Cliente>): Observable<Cliente>;
  delete(id: number): Observable<void>;
}
