import { Observable } from 'rxjs';
import { ReporteEstadoCuenta, ReporteQuery } from '../models/reporte.model';

export interface ReporteRepository {
  estadoCuenta(query: ReporteQuery): Observable<ReporteEstadoCuenta>;
}
