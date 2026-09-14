export interface LoginResponse {
  valido: boolean;
  mensaje: string;
}

export interface TransaccionResponse {
  id: number;
  estatus: string;
  referencia: string;
  operacion: string;
  importe: number;
  cliente: string;
}

export interface PaginaResponse<T> {
  contenido: T[];
  pagina: number;
  registrosPorPagina: number;
  totalRegistros: number;
  totalPaginas: number;
  ultimaPagina: boolean;
}
