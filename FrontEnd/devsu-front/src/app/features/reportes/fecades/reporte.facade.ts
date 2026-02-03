import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ReporteApiRepository } from '../repositories/reporte-api.repository';
import { ReporteEstadoCuenta, ReporteQuery } from '../models/reporte.model';

type UiState<T> = { data: T | null; loading: boolean; error: string | null };

@Injectable({ providedIn: 'root' })
export class ReporteFacade {
  private readonly dataSubject = new BehaviorSubject<ReporteEstadoCuenta | null>(null);
  private readonly loadingSubject = new BehaviorSubject<boolean>(false);
  private readonly errorSubject = new BehaviorSubject<string | null>(null);

  readonly vm$ = new BehaviorSubject<UiState<ReporteEstadoCuenta>>({
    data: null,
    loading: false,
    error: null,
  });

  constructor(private readonly repo: ReporteApiRepository) {}

  clearError() {
    this.errorSubject.next(null);
    this.vm$.next({ ...this.vm$.value, error: null });
  }

  buscar(query: ReporteQuery) {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);
    this.vm$.next({ data: this.dataSubject.value, loading: true, error: null });

    this.repo.estadoCuenta(query)
      .pipe(finalize(() => {
        this.loadingSubject.next(false);
        this.vm$.next({ data: this.dataSubject.value, loading: false, error: this.errorSubject.value });
      }))
      .subscribe({
        next: (res) => {
          this.dataSubject.next(res);
          this.vm$.next({ data: res, loading: false, error: null });
        },
        error: (e) => {
          const msg = e?.message ?? 'Error generando reporte';
          this.errorSubject.next(msg);
          this.vm$.next({ data: null, loading: false, error: msg });
        },
      });
  }
}
