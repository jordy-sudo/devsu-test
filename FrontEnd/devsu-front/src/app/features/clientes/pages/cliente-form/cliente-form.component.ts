import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteFacade } from '../../fecades/cliente.facade';
import { ClienteCreate } from '../../models/cliente.model';

@Component({
  standalone: true,
  selector: 'app-cliente-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.css'],
})
export class ClienteFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  readonly facade = inject(ClienteFacade);

  isEdit = false;
  id: number | null = null;

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(3)]],
    genero: ['', [Validators.required]],
    edad: [18, [Validators.required, Validators.min(0), Validators.max(120)]],
    identificacion: ['', [Validators.required, Validators.minLength(5)]],
    direccion: ['', [Validators.required, Validators.minLength(5)]],
    telefono: ['', [Validators.required, Validators.minLength(7)]],
    contrasena: ['', [Validators.required, Validators.minLength(4)]],
    estado: [{ value: true, disabled: true }]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.isEdit = !!idParam;
    this.id = idParam ? Number(idParam) : null;

    if (this.isEdit && this.id != null && !Number.isNaN(this.id)) {
      this.facade.getById(this.id, (cliente) => {
        this.form.patchValue({
          nombre: cliente.nombre,
          genero: cliente.genero,
          edad: cliente.edad,
          identificacion: cliente.identificacion,
          direccion: cliente.direccion,
          telefono: cliente.telefono,
          contrasena: cliente.contrasena,
          estado: cliente.estado,
        });
      });
    }
  }

  volver() {
    this.router.navigate(['/clientes']);
  }

  guardar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const payload = this.form.getRawValue() as ClienteCreate;

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
    if (c.errors['max']) return `Valor máximo ${c.errors['max'].max}`;
    return 'Campo inválido';
  }
}
