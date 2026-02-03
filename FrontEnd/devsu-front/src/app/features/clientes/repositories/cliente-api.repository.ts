import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API } from '../../../core/config/api.config';
import { Cliente } from '../models/cliente.model';
import { ClienteRepository } from './cliente.repository';

@Injectable({ providedIn: 'root' })
export class ClienteApiRepository implements ClienteRepository {
  constructor(private http: HttpClient) {}

  list(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(API.clientes());
  }

  get(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${API.clientes()}/${id}`);
  }

  create(payload: Partial<Cliente>): Observable<Cliente> {
    return this.http.post<Cliente>(API.clientes(), payload);
  }

  update(id: number, payload: Partial<Cliente>): Observable<Cliente> {
    return this.http.put<Cliente>(`${API.clientes()}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API.clientes()}/${id}`);
  }
}
