import { Curso } from './curso.model';
import { Estudiante } from './estudiante.model';

export type EstadoAsistencia = 'Presente' | 'Tarde' | 'Falta';

export interface Asistencia {
  id?: number;
  estudiante: Estudiante;
  curso: Curso;
  fecha: string; // ISO yyyy-MM-dd
  estado: EstadoAsistencia | string;
  observaciones?: string;
}

// DTO que espera el backend en POST/PUT /api/asistencias (AsistenciaRequest record)
export interface AsistenciaRequest {
  estudianteId: number;
  cursoId: number;
  fecha: string;
  estado: string;
  observaciones?: string;
}

export interface AsistenciaFiltro {
  fecha?: string;
  estado?: string;
  estudianteId?: number;
  cursoId?: number;
}
