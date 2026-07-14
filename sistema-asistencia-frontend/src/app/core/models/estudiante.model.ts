import { Usuario } from './auth.model';

export interface Estudiante {
  id?: number;
  codigo: string;
  nombres: string;
  apellidos: string;
  carrera?: string;
  usuario?: Usuario | null;
}
