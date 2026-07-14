import { Usuario } from './auth.model';

export interface Curso {
  id?: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  creditos?: number;
  profesor?: string;
  ciclo?: string;
  docente?: Usuario | null;
}

// DTO que espera el backend en POST/PUT /api/cursos (CursoRequest.java)
export interface CursoRequest {
  codigo: string;
  nombre: string;
  descripcion?: string;
  creditos?: number;
  profesor?: string;
  ciclo?: string;
  docenteId?: number | null;
}
