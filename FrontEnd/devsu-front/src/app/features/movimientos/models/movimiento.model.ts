export interface Movimiento {
    movimientoId: number;
    fecha: string;          
    tipoMovimiento: 'CREDITO' | 'DEBITO';
    valor: number;
    saldo: number;
    cuentaId: number;
  }
  
  export type MovimientoCreate = Omit<Movimiento, 'movimientoId' | 'saldo'>;
  