import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API } from '../../../core/config/api.config';
import { ReporteEstadoCuenta, ReporteQuery } from '../models/reporte.model';
import { ReporteRepository } from './reporte.repository';

@Injectable({ providedIn: 'root' })
export class ReporteApiRepository implements ReporteRepository {
  constructor(private http: HttpClient) {}

  estadoCuenta(query: ReporteQuery): Observable<ReporteEstadoCuenta> {
    const params = new HttpParams()
      .set('clienteId', String(query.clienteId))
      .set('desde', query.desde)
      .set('hasta', query.hasta);

    return this.http.get<ReporteEstadoCuenta>(API.reportes(), { params });
  }
}
