import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MovimientoFacade } from '../../fecades/movimiento.facade';
import { MovimientoCreate } from '../../models/movimiento.model';

@Component({
  standalone: true,
  selector: 'app-movimiento-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movimiento-form.component.html',
  styleUrls: ['./movimiento-form.component.css'],
})
export class MovimientoFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  readonly facade = inject(MovimientoFacade);

  form = this.fb.group({
    cuentaId: [null as number | null, [Validators.required]],
    tipoMovimiento: ['', [Validators.required]],
    valor: [0, [Validators.required, Validators.min(1)]],
  });

  volver() {
    this.router.navigate(['/movimientos']);
  }

  guardar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const payload = this.form.getRawValue() as MovimientoCreate;

    this.facade.create(payload, () => this.volver());
  }

  msg(name: string): string | null {
    const c = this.form.get(name);
    if (!c || !c.touched || !c.errors) return null;

    if (c.errors['required']) return 'Campo obligatorio';
    if (c.errors['min']) return 'Debe ser mayor a 0';
    return 'Campo inválido';
  }
}
