export interface ReporteCuentaItem {
    numeroCuenta: string;
    tipo: string;
    saldo: number;
  }
  
  export interface ReporteEstadoCuenta {
    clienteId: number;
    cliente: string;
    fechaInicio: string; // yyyy-mm-dd
    fechaFin: string;    // yyyy-mm-dd
    cuentas: ReporteCuentaItem[];
    totalCreditos: number;
    totalDebitos: number;
    pdfBase64?: string; 
  }
  
  export interface ReporteQuery {
    clienteId: number;
    fechaInicio: string; // yyyy-mm-dd
    fechaFin: string;    // yyyy-mm-dd
  }
  