import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GlobalErrorService } from '../../../core/ui/global-error.service';

@Component({
  standalone: true,
  selector: 'app-global-error',
  imports: [CommonModule],
  templateUrl: './global-error.component.html',
  styleUrls: ['./global-error.component.css'],
})
export class GlobalErrorComponent {
  readonly service = inject(GlobalErrorService);
  readonly state$ = this.service.state$;

  close() {
    this.service.hide();
  }
}
