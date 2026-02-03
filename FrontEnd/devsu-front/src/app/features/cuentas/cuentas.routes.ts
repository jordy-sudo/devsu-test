import { Routes } from '@angular/router';
import { CuentaListComponent } from './pages/cuenta-list/cuenta-list.component';
import { CuentaFormComponent } from './pages/cuenta-form/cuenta-form.component';

export const CUENTAS_ROUTES: Routes = [
  { path: '', component: CuentaListComponent },
  { path: 'nuevo', component: CuentaFormComponent },
  { path: 'editar/:id', component: CuentaFormComponent },
];
