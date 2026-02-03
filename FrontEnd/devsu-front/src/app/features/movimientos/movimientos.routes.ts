import { Routes } from '@angular/router';
import { MovimientoListComponent } from './pages/movimiento-list/movimiento-list.component';
import { MovimientoFormComponent } from './pages/movimiento-form/movimiento-form.component';

export const MOVIMIENTOS_ROUTES: Routes = [
  { path: '', component: MovimientoListComponent },
  { path: 'nuevo', component: MovimientoFormComponent },
];
