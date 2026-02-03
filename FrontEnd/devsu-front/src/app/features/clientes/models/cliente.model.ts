export interface Cliente {
    clienteId: number;
    nombre: string;
    genero: string;
    edad: number;
    identificacion: string;
    direccion: string;
    telefono: string;
    contrasena: string;
    estado: boolean;
  }
  
  export type ClienteCreate = Omit<Cliente, 'clienteId'>;
  export type ClienteUpdate = Partial<Omit<Cliente, 'clienteId'>>;
  