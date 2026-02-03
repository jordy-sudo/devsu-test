import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { GlobalErrorService } from '../ui/global-error.service';
import { ApiError } from './api-error.model';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const globalError = inject(GlobalErrorService);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 0) {
        globalError.show(
          { status: 0, error: 'Network Error', message: 'No se pudo conectar con el servidor.' },
          'No se pudo conectar con el servidor.'
        );
        return throwError(() => err);
      }

      const body = (err.error ?? {}) as ApiError;

      const apiErr: ApiError = {
        status: err.status,
        error: body.error ?? err.statusText ?? 'Error',
        message: body.message ?? 'Ocurrió un error',
        timestamp: body.timestamp,
      };

      globalError.show(apiErr, 'Ocurrió un error');
      return throwError(() => apiErr); 
    })
  );
};
