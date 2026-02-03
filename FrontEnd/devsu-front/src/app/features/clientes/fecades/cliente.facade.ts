import { Injectable } from '@angular/core';
import { BehaviorSubject, combineLatest } from 'rxjs';
import { distinctUntilChanged, finalize, map, shareReplay } from 'rxjs/operators';
import { Cliente } from '../models/cliente.model';
import { ClienteApiRepository } from '../repositories/cliente-api.repository';

type UiState<T> = { data: T; loading: boolean; error: string | null };

@Injectable({ providedIn: 'root' })
export class ClienteFacade {
  private readonly dataSubject = new BehaviorSubject<Cliente[]>([]);
  private readonly loadingSubject = new BehaviorSubject<boolean>(false);
  private readonly errorSubject = new BehaviorSubject<string | null>(null);
  private readonly searchSubject = new BehaviorSubject<string>('');

  private readonly search$ = this.searchSubject.asObservable().pipe(
    map(v => v.trim().toLowerCase()),
    distinctUntilChanged()
  );

  readonly vm$ = combineLatest([
    this.dataSubject.asObservable(),
    this.loadingSubject.asObservable(),
    this.errorSubject.asObservable(),
    this.search$,
  ]).pipe(
    map(([data, loading, error, search]) => {
      const filtered =
        !search
          ? data
          : data.filter((c) =>
              `${c.nombre} ${c.identificacion} ${c.telefono} ${c.direccion}`
                .toLowerCase()
                .includes(search)
            );

      return { data: filtered, loading, error } as UiState<Cliente[]>;
    }),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  constructor(private readonly repo: ClienteApiRepository) {}

  setSearch(value: string) {
    this.searchSubject.next(value ?? '');
  }

  clearError() {
    this.errorSubject.next(null);
  }

  load() {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo
      .list()
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: (res) => this.dataSubject.next(res ?? []),
        error: (e) => this.errorSubject.next(e?.message ?? 'Error cargando clientes'),
      });
  }

  getById(id: number, onOk: (cliente: Cliente) => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo
      .get(id)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: (cliente) => onOk(cliente),
        error: (e) => this.errorSubject.next(e?.message ?? 'Error cargando cliente'),
      });
  }

  create(payload: Partial<Cliente>, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo
      .create(payload)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => {
          this.load();
          onOk?.();
        },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error creando cliente'),
      });
  }

  update(id: number, payload: Partial<Cliente>, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo
      .update(id, payload)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => {
          this.load();
          onOk?.();
        },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error actualizando cliente'),
      });
  }

  remove(id: number, onOk?: () => void) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    this.repo
      .delete(id)
      .pipe(finalize(() => this.loadingSubject.next(false)))
      .subscribe({
        next: () => {
          this.load();
          onOk?.();
        },
        error: (e) => this.errorSubject.next(e?.message ?? 'Error eliminando cliente'),
      });
  }
}
