import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MovimientoFacade } from '../../fecades/movimiento.facade';

@Component({
  standalone: true,
  selector: 'app-movimiento-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './movimiento-list.component.html',
  styleUrls: ['./movimiento-list.component.css'],
})
export class MovimientoListComponent implements OnInit {
  private readonly router = inject(Router);
  readonly facade = inject(MovimientoFacade);

  search = '';
  readonly vm$ = this.facade.vm$;

  ngOnInit(): void {
    this.facade.load();
  }

  nuevo() {
    this.router.navigate(['/movimientos/nuevo']);
  }

  eliminar(id: number) {
    if (confirm('¿Eliminar este movimiento?')) {
      this.facade.remove(id);
    }
  }
}
