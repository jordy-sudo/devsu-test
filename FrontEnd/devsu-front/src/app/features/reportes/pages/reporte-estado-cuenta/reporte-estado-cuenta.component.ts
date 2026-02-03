import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ReporteFacade } from '../../fecades/reporte.facade';
import { ClienteApiRepository } from '../../../clientes/repositories/cliente-api.repository'; 
import { Cliente } from '../../../clientes/models/cliente.model';
import { ReporteQuery } from '../../models/reporte.model';

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
    fechaInicio: ['', [Validators.required]], // yyyy-mm-dd
    fechaFin: ['', [Validators.required]],    // yyyy-mm-dd
  });

  ngOnInit(): void {
    this.clientesRepo.list().subscribe({
      next: (res) => (this.clientes = res ?? []),
    });

    // opcional: fechas por defecto (hoy)
    const today = new Date().toISOString().slice(0, 10);
    this.form.patchValue({ fechaInicio: today, fechaFin: today });
  }

  generar() {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const q = this.form.getRawValue() as ReporteQuery;
    this.facade.buscar(q);
  }

  descargarPdf(base64?: string) {
    if (!base64) return;

    // limpia posible prefijo data:
    const clean = base64.includes('base64,') ? base64.split('base64,')[1] : base64;

    const byteChars = atob(clean);
    const byteNumbers = new Array(byteChars.length);
    for (let i = 0; i < byteChars.length; i++) byteNumbers[i] = byteChars.charCodeAt(i);
    const blob = new Blob([new Uint8Array(byteNumbers)], { type: 'application/pdf' });

    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `estado-cuenta_${this.form.value.clienteId}_${this.form.value.fechaInicio}_${this.form.value.fechaFin}.pdf`;
    a.click();
    URL.revokeObjectURL(url);
  }

  msg(name: string): string | null {
    const c = this.form.get(name);
    if (!c || !c.touched || !c.errors) return null;
    if (c.errors['required']) return 'Este campo es obligatorio';
    return 'Campo inválido';
  }
}
