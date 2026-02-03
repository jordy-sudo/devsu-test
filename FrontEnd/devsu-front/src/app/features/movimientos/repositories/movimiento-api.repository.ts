import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API } from '../../../core/config/api.config';
import { Movimiento, MovimientoCreate } from '../models/movimiento.model';
import { MovimientoRepository } from './movimiento.repository';

@Injectable({ providedIn: 'root' })
export class MovimientoApiRepository implements MovimientoRepository {
  constructor(private http: HttpClient) {}

  list(): Observable<Movimiento[]> {
    return this.http.get<Movimiento[]>(API.movimientos());
  }

  get(id: number): Observable<Movimiento> {
    return this.http.get<Movimiento>(`${API.movimientos()}/${id}`);
  }

  create(payload: MovimientoCreate): Observable<Movimiento> {
    return this.http.post<Movimiento>(API.movimientos(), payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API.movimientos()}/${id}`);
  }
}
