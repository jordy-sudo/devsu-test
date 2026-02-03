import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ApiError } from '../http/api-error.model';

export interface GlobalErrorState {
  visible: boolean;
  title: string;
  message: string;
  status?: number;
  raw?: ApiError | unknown;
}

@Injectable({ providedIn: 'root' })
export class GlobalErrorService {
  private readonly stateSubject = new BehaviorSubject<GlobalErrorState>({
    visible: false,
    title: '',
    message: '',
  });

  readonly state$ = this.stateSubject.asObservable();

  private timer: any = null;

  show(err: ApiError | any, fallbackMessage = 'Ocurrió un error') {
    const status = err?.status;
    const title = err?.error ?? `Error${status ? ` ${status}` : ''}`;
    const message = err?.message ?? fallbackMessage;

    this.stateSubject.next({
      visible: true,
      title,
      message,
      status,
      raw: err,
    });

    this.clearTimer();
    this.timer = setTimeout(() => this.hide(), 6000);
  }

  hide() {
    this.clearTimer();
    this.stateSubject.next({
      ...this.stateSubject.value,
      visible: false,
    });
  }

  private clearTimer() {
    if (this.timer) {
      clearTimeout(this.timer);
      this.timer = null;
    }
  }
}
