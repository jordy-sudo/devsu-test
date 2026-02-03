import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest } from 'rxjs';
import { distinctUntilChanged, finalize, map, shareReplay } from 'rxjs/operators';
import { Movimiento, MovimientosQuery, MovimientoCreate } from '../models/movimiento.model';
import { MovimientoApiRepository } from '../repositories/movimiento-api.repository';

type UiState<T> = { data: T; loading: boolean; error: string | null; searched: boolean };

@Injectable({ providedIn: 'root' })
export class MovimientoFacade {
  private readonly dataSubject = new BehaviorSubject<Movimiento[]>([]);
  private readonly loadingSubject = new BehaviorSubject<boolean>(false);
  private readonly errorSubject = new BehaviorSubject<string | null>(null);
  private readonly searchSubject = new BehaviorSubject<string>('');
  private readonly searchedSubject = new BehaviorSubject<boolean>(false);

  private lastQuery: MovimientosQuery | null = null;

  private readonly search$ = this.searchSubject.asObservable().pipe(
    map(v => (v ?? '').trim().toLowerCase()),
    distinctUntilChanged()
  );

  readonly vm$ = combineLatest([
    this.dataSubject.asObservable(),
    this.loadingSubject.asObservable(),
    this.errorSubject.asObservable(),
    this.search$,
    this.searchedSubject.asObservable(),
  ]).pipe(
    map(([data, loading, error, search, searched]) => {
      const filtered = !search
        ? data
        : data.filter((m) =>
            `${m.fecha} ${m.tipoMovimiento} ${m.valor} ${m.saldo} ${m.cuentaId}`
              .toLowerCase()
              .includes(search)
          );

      return { data: filtered, loading, error, searched } as UiState<Movimiento[]>;
    }),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  constructor(private readonly repo: MovimientoApiRepository) {}

  setSearch(value: string) {
    this.searchSubject.next(value ?? '');
  }

  clearError() {
    this.errorSubject.next(null);
  }

  reset() {
    this.dataSubject.next([]);
    this.errorSubject.next(null);
    this.loadingSubject.next(false);
    this.searchedSubject.next(false);
    this.searchSubject.next('');
    this.lastQuery = null;
  }

  buscar(query: MovimientosQuery) {
    this.lastQuery = query;

    this.loadingSubject.next(true);
    this.errorSubject.next(null);
    this.searchedSubject.next(true);

    this.repo.list(query)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: (res) => this.dataSubject.next(res ?? []),
        error: (e) => this.errorSubject.next(e?.message ?? 'Error cargando movimientos'),
      });
  }

  create(payload: MovimientoCreate, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.create(payload)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => {
          if (this.lastQuery) this.buscar(this.lastQuery);
          onOk?.();
        },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error creando movimiento'),
      });
  }

  remove(id: number) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.delete(id)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => {
          if (this.lastQuery) this.buscar(this.lastQuery);
        },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error eliminando movimiento'),
      });
  }
}
