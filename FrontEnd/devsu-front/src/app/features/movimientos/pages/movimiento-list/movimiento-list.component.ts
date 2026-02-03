import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { MovimientoFacade } from '../../fecades/movimiento.facade';
import { MovimientosQuery } from '../../models/movimiento.model';

import { ClienteApiRepository } from '../../../clientes/repositories/cliente-api.repository';
import { Cliente } from '../../../clientes/models/cliente.model';

function yyyyMmDdLocal(d: Date): string {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
}

@Component({
  standalone: true,
  selector: 'app-movimiento-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movimiento-list.component.html',
  styleUrls: ['./movimiento-list.component.css'],
})
export class MovimientoListComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);

  readonly facade = inject(MovimientoFacade);
  private readonly clientesRepo = inject(ClienteApiRepository);

  clientes: Cliente[] = [];
  readonly vm$ = this.facade.vm$;

  form = this.fb.group({
    clienteId: [null as number | null, [Validators.required]],
    desde: ['', [Validators.required]], // yyyy-mm-dd
    hasta: ['', [Validators.required]], // yyyy-mm-dd
  });

  search = '';

  ngOnInit(): void {
    this.facade.reset();

    this.clientesRepo.list().subscribe({
      next: (res) => {
        this.clientes = res ?? [];
        // opcional: setear primer cliente
        const current = this.form.get('clienteId')?.value;
        if (current == null && this.clientes.length > 0) {
          this.form.patchValue({ clienteId: this.clientes[0].clienteId });
        }
      },
      error: (err) => {
        console.error('Error cargando clientes', err);
      },
    });

    // fechas por defecto (hoy) — local (sin UTC)
    const today = yyyyMmDdLocal(new Date());
    this.form.patchValue({ desde: today, hasta: today });
  }

  nuevo() {
    this.router.navigate(['/movimientos/nuevo']);
  }

  buscar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const v = this.form.getRawValue();

    // validación adicional: desde <= hasta
    if (v.desde && v.hasta && v.desde > v.hasta) {
      alert('La fecha "desde" no puede ser mayor que "hasta"');
      return;
    }

    const q: MovimientosQuery = {
      clienteId: v.clienteId!,
      desde: v.desde!,
      hasta: v.hasta!,
      // q: (v.q ?? '').trim() || undefined,
    };

    this.facade.buscar(q);
  }

  eliminar(id: number) {
    if (!confirm('¿Eliminar este movimiento?')) return;
    this.facade.remove(id);
  }

  onSearchLocal(txt: string) {
    this.search = txt;
    this.facade.setSearch?.(txt);
  }

  msg(name: string): string | null {
    const c = this.form.get(name);
    if (!c || !c.touched || !c.errors) return null;
    if (c.errors['required']) return 'Este campo es obligatorio';
    return 'Campo inválido';
  }
}
