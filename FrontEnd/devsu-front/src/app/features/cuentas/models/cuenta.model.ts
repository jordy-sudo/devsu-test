export interface Cuenta {
    cuentaId: number;
    numeroCuenta: string;
    tipoCuenta: string;     
    saldoInicial: number;
    estado: boolean;
  
    clienteId?: number;
  }
  
  export type CuentaCreate = Omit<Cuenta, 'cuentaId'>;
  export type CuentaUpdate = Partial<Omit<Cuenta, 'cuentaId'>>;
  