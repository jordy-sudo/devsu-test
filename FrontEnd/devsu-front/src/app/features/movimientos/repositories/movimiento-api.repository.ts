import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API } from '../../../core/config/api.config';
import { Movimiento, MovimientoCreate, MovimientosQuery } from '../models/movimiento.model';

@Injectable({ providedIn: 'root' })
export class MovimientoApiRepository {
  constructor(private http: HttpClient) {}

  create(payload: MovimientoCreate): Observable<Movimiento> {
    return this.http.post<Movimiento>(API.movimientos(), payload);
  }

  list(query: MovimientosQuery): Observable<Movimiento[]> {
    const params = new HttpParams()
      .set('desde', query.desde)
      .set('hasta', query.hasta)
      .set('clienteId', String(query.clienteId)); // cambia el nombre si tu backend usa otro

    return this.http.get<Movimiento[]>(API.movimientos(), { params });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API.movimientos()}/${id}`);
  }
}

