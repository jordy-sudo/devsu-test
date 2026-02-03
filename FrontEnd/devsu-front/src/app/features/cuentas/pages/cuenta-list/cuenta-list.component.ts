import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CuentaFacade } from '../../fecades/cuenta.facade';

@Component({
  standalone: true,
  selector: 'app-cuenta-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './cuenta-list.component.html',
  styleUrls: ['./cuenta-list.component.css'],
})
export class CuentaListComponent implements OnInit {
  private readonly router = inject(Router);
  readonly facade = inject(CuentaFacade);

  search = '';
  readonly vm$ = this.facade.vm$;

  ngOnInit(): void {
    this.facade.load();
  }

  nuevo() {
    this.router.navigate(['/cuentas/nuevo']);
  }

  editar(id: number) {
    this.router.navigate(['/cuentas/editar', id]);
  }

  eliminar(id: number) {
    if (confirm('¿Seguro que deseas eliminar esta cuenta?')) {
      this.facade.remove(id);
    }
  }
}
