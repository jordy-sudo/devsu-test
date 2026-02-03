export interface Movimiento {
    movimientoId: number;
    fecha: string;          
    tipoMovimiento: 'CREDITO' | 'DEBITO';
    valor: number;
    saldo: number;
    cuentaId: number;
  }
  
  export type MovimientoCreate = Omit<Movimiento, 'movimientoId' | 'saldo'>;
  
  export interface MovimientosQuery {
    desde: string;     // yyyy-mm-dd
    hasta: string;     // yyyy-mm-dd
    clienteId: number; // requerido en tu caso
  }