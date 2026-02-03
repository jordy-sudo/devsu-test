import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API } from '../../../core/config/api.config';
import { Cuenta, CuentaCreate, CuentaUpdate } from '../models/cuenta.model';
import { CuentaRepository } from './cuenta.repository';

@Injectable({ providedIn: 'root' })
export class CuentaApiRepository implements CuentaRepository {
  constructor(private http: HttpClient) {}

  list(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(API.cuentas());
  }

  get(id: number): Observable<Cuenta> {
    return this.http.get<Cuenta>(`${API.cuentas()}/${id}`);
  }

  create(payload: CuentaCreate): Observable<Cuenta> {
    return this.http.post<Cuenta>(API.cuentas(), payload);
  }

  update(id: number, payload: CuentaUpdate): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${API.cuentas()}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API.cuentas()}/${id}`);
  }
}
