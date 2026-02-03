import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest } from 'rxjs';
import { distinctUntilChanged, finalize, map, shareReplay } from 'rxjs/operators';
import { Movimiento, MovimientoCreate } from '../models/movimiento.model';
import { MovimientoApiRepository } from '../repositories/movimiento-api.repository';

type UiState<T> = { data: T; loading: boolean; error: string | null };

@Injectable({ providedIn: 'root' })
export class MovimientoFacade {
  private readonly dataSubject = new BehaviorSubject<Movimiento[]>([]);
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
        : data.filter((m) =>
            `${m.tipoMovimiento} ${m.valor} ${m.fecha} ${m.cuentaId}`
              .toLowerCase()
              .includes(search)
          );

      return { data: filtered, loading, error } as UiState<Movimiento[]>;
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

  load() {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.list()
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
        next: () => { this.load(); onOk?.(); },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error creando movimiento'),
      });
  }

  remove(id: number) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo.delete(id)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => this.load(),
        error: (e) => this.errorSubject.next(e?.message ?? 'Error eliminando movimiento'),
      });
  }
}
