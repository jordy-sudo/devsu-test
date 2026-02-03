import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest } from 'rxjs';
import { distinctUntilChanged, finalize, map, shareReplay } from 'rxjs/operators';
import { Cuenta, CuentaCreate, CuentaUpdate } from '../models/cuenta.model';
import { CuentaApiRepository } from '../repositories/cuenta-api.repository';

type UiState<T> = { data: T; loading: boolean; error: string | null };

@Injectable({ providedIn: 'root' })
export class CuentaFacade {
  private readonly dataSubject = new BehaviorSubject<Cuenta[]>([]);
  private readonly loadingSubject = new BehaviorSubject<boolean>(false);
  private readonly errorSubject = new BehaviorSubject<string | null>(null);
  private readonly searchSubject = new BehaviorSubject<string>('');

  private readonly search$ = this.searchSubject.asObservable().pipe(
    map(v => (v ?? '').trim().toLowerCase()),
    distinctUntilChanged()
  );

  readonly vm$ = combineLatest([
    this.dataSubject.asObservable(),
    this.loadingSubject.asObservable(),
    this.errorSubject.asObservable(),
    this.search$,
  ]).pipe(
    map(([data, loading, error, search]) => {
      const filtered = !search
        ? data
        : data.filter((c) =>
            `${c.numeroCuenta} ${c.tipoCuenta} ${c.saldoInicial} ${c.estado}`
              .toLowerCase()
              .includes(search)
          );

      return { data: filtered, loading, error } as UiState<Cuenta[]>;
    }),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  constructor(private readonly repo: CuentaApiRepository) {}

  setSearch(value: string) {
    this.searchSubject.next(value ?? '');
  }

  clearError() {
    this.errorSubject.next(null);
  }

  load() {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.list()
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: (res) => this.dataSubject.next(res ?? []),
        error: (e) => this.errorSubject.next(e?.message ?? 'Error cargando cuentas'),
      });
  }

  getById(id: number, onOk: (cuenta: Cuenta) => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.get(id)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: onOk,
        error: (e) => this.errorSubject.next(e?.message ?? 'Error cargando cuenta'),
      });
  }

  create(payload: CuentaCreate, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.create(payload)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => { this.load(); onOk?.(); },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error creando cuenta'),
      });
  }

  update(id: number, payload: CuentaUpdate, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.update(id, payload)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => { this.load(); onOk?.(); },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error actualizando cuenta'),
      });
  }

  remove(id: number, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.delete(id)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => { this.load(); onOk?.(); },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error eliminando cuenta'),
      });
  }
}
