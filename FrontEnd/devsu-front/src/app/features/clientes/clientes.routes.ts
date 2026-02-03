import { Routes } from '@angular/router';
import { ClienteListComponent } from './pages/cliente-list/cliente-list.component';
import { ClienteFormComponent } from './pages/cliente-form/cliente-form.component';

export const CLIENTES_ROUTES: Routes = [
  { path: '', component: ClienteListComponent },
  { path: 'nuevo', component: ClienteFormComponent },
  { path: 'editar/:id', component: ClienteFormComponent },
];
