import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Asistencia, AsistenciaFiltro, AsistenciaRequest } from '../models/asistencia.model';
import { Curso } from '../models/curso.model';

@Injectable({ providedIn: 'root' })
export class AsistenciaService {
  private readonly apiUrl = `${environment.apiUrl}/asistencias`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Asistencia[]> {
    return this.http.get<Asistencia[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Asistencia> {
    return this.http.get<Asistencia>(`${this.apiUrl}/${id}`);
  }

  buscar(filtro: AsistenciaFiltro): Observable<Asistencia[]> {
    let params = new HttpParams();
    Object.entries(filtro).forEach(([clave, valor]) => {
      if (valor !== undefined && valor !== null && valor !== '') {
        params = params.set(clave, String(valor));
      }
    });
    return this.http.get<Asistencia[]>(`${this.apiUrl}/buscar`, { params });
  }

  crear(asistencia: AsistenciaRequest): Observable<Asistencia> {
    return this.http.post<Asistencia>(this.apiUrl, asistencia);
  }

  actualizar(id: number, asistencia: AsistenciaRequest): Observable<Asistencia> {
    return this.http.put<Asistencia>(`${this.apiUrl}/${id}`, asistencia);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Endpoints exclusivos del rol ALUMNO (AlumnoController.java)
  misCursos(): Observable<Curso[]> {
    return this.http.get<Curso[]>(`${environment.apiUrl}/mis-cursos`);
  }

  miAsistencia(): Observable<Asistencia[]> {
    return this.http.get<Asistencia[]>(`${environment.apiUrl}/mi-asistencia`);
  }
}
