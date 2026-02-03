export interface ReporteCuentaItem {
  numeroCuenta: string;
  tipoCuenta: string;
  saldoInicial: number;
  saldoDisponible: number;
  estado: boolean;
}

export interface ReporteMovimientoItem {
  id: number;
  fecha: string;           
  numeroCuenta: string;
  tipoMovimiento: string;
  valor: number;           
  saldo: number;          
}

export interface ReporteEstadoCuenta {
  cliente: string;
  identificacion: string;  
  desde: string;           // yyyy-mm-dd
  hasta: string;           // yyyy-mm-dd
  cuentas: ReporteCuentaItem[];
  movimientos: ReporteMovimientoItem[];
  totalCreditos: number;
  totalDebitos: number;
  pdfBase64?: string;
}

export interface ReporteQuery {
  clienteId: number;
  desde: string; // yyyy-mm-dd
  hasta: string; // yyyy-mm-dd
}
