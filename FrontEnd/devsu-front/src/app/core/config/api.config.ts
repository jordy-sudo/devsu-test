import { environment } from "../../../environments/environment";

export const API = {
  baseUrl: environment.apiBaseUrl,

  clientes: () => `${environment.apiBaseUrl}/clientes`,
  cuentas: () => `${environment.apiBaseUrl}/cuentas`,
  movimientos: () => `${environment.apiBaseUrl}/movimientos`,
  reportes: () => `${environment.apiBaseUrl}/reportes`,
};
