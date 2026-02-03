import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CuentaFacade } from '../../fecades/cuenta.facade';
import { CuentaCreate } from '../../models/cuenta.model';

@Component({
  standalone: true,
  selector: 'app-cuenta-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cuenta-form.component.html',
  styleUrls: ['./cuenta-form.component.css'],
})
export class CuentaFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  readonly facade = inject(CuentaFacade);

  isEdit = false;
  id: number | null = null;

  form = this.fb.group({
    numeroCuenta: ['', [Validators.required, Validators.minLength(3)]],
    tipoCuenta: ['', [Validators.required]],
    saldoInicial: [0, [Validators.required, Validators.min(0)]],
    estado: [{ value: true, disabled: true }], // readonly por defecto
    // clienteId: [null as number | null, [Validators.required]], // si aplica
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.isEdit = !!idParam;
    this.id = idParam ? Number(idParam) : null;

    if (this.isEdit && this.id != null && !Number.isNaN(this.id)) {
      this.facade.getById(this.id, (cuenta) => {
        this.form.patchValue({
          numeroCuenta: cuenta.numeroCuenta,
          tipoCuenta: cuenta.tipoCuenta,
          saldoInicial: cuenta.saldoInicial,
          estado: cuenta.estado,
          // clienteId: cuenta.clienteId ?? null,
        });
      });
    }
  }

  volver() {
    this.router.navigate(['/cuentas']);
  }

  guardar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    // OJO: estado está disabled => usar getRawValue()
    const payload = this.form.getRawValue() as CuentaCreate;

    if (!this.isEdit) {
      this.facade.create(payload, () => this.volver());
      return;
    }

    if (this.id == null || Number.isNaN(this.id)) return;
    this.facade.update(this.id, payload, () => this.volver());
  }

  msg(name: string): string | null {
    const c = this.form.get(name);
    if (!c || !c.touched || !c.errors) return null;

    if (c.errors['required']) return 'Este campo es obligatorio';
    if (c.errors['minlength']) return `Mínimo ${c.errors['minlength'].requiredLength} caracteres`;
    if (c.errors['min']) return `Valor mínimo ${c.errors['min'].min}`;
    return 'Campo inválido';
  }
}
