import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReporteFacade } from '../../fecades/reporte.facade';
import { ClienteApiRepository } from '../../../clientes/repositories/cliente-api.repository';
import { Cliente } from '../../../clientes/models/cliente.model';
import { ReporteQuery } from '../../models/reporte.model';

function yyyyMmDdLocal(d: Date): string {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
}

@Component({
  standalone: true,
  selector: 'app-reporte-estado-cuenta',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reporte-estado-cuenta.component.html',
  styleUrls: ['./reporte-estado-cuenta.component.css'],
})
export class ReporteEstadoCuentaComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  readonly facade = inject(ReporteFacade);
  private readonly clientesRepo = inject(ClienteApiRepository);

  clientes: Cliente[] = [];
  readonly vm$ = this.facade.vm$;

  form = this.fb.group({
    clienteId: [null as number | null, [Validators.required]],
    desde: ['', [Validators.required]], // yyyy-mm-dd
    hasta: ['', [Validators.required]], // yyyy-mm-dd
  });

  ngOnInit(): void {
    this.clientesRepo.list().subscribe({
      next: (res) => (this.clientes = res ?? []),
      error: (err) => console.error('Error cargando clientes', err),
    });

    const today = yyyyMmDdLocal(new Date());
    this.form.patchValue({ desde: today, hasta: today });
  }

  generar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const { desde, hasta } = this.form.getRawValue();

    if (desde && hasta && desde > hasta) {
      alert('La fecha "desde" no puede ser mayor que "hasta"');
      return;
    }

    const q = this.form.getRawValue() as ReporteQuery; // { clienteId, desde, hasta }
    this.facade.buscar(q);
  }

  descargarPdf(base64: string) {
    if (!base64) return;

    const clean = base64.includes('base64,') ? base64.split('base64,')[1] : base64;

    const byteChars = atob(clean);
    const byteNumbers = new Array(byteChars.length);
    for (let i = 0; i < byteChars.length; i++) {
      byteNumbers[i] = byteChars.charCodeAt(i);
    }

    const blob = new Blob([new Uint8Array(byteNumbers)], { type: 'application/pdf' });
    const url = URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;

    const clienteId = this.form.get('clienteId')?.value ?? 'cliente';
    const fi = this.form.get('desde')?.value ?? 'desde';
    const ff = this.form.get('hasta')?.value ?? 'hasta';

    a.download = `estado-cuenta_${clienteId}_${fi}_${ff}.pdf`;
    a.click();

    setTimeout(() => URL.revokeObjectURL(url), 1000);
  }

  msg(name: string): string | null {
    const c = this.form.get(name);
    if (!c || !c.touched || !c.errors) return null;
    if (c.errors['required']) return 'Este campo es obligatorio';
    return 'Campo inválido';
  }
}
