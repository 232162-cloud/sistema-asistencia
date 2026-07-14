export type Rol = 'ADMIN' | 'DOCENTE' | 'ALUMNO';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  rol: Rol;
  emision: string;
  expiracion: string;
}

export interface Usuario {
  id?: number;
  username: string;
  rol: Rol;
  nombreCompleto?: string;
}
