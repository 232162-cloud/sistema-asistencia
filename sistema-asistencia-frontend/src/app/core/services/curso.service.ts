import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Curso, CursoRequest } from '../models/curso.model';

@Injectable({ providedIn: 'root' })
export class CursoService {
  private readonly apiUrl = `${environment.apiUrl}/cursos`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Curso> {
    return this.http.get<Curso>(`${this.apiUrl}/${id}`);
  }

  buscar(filtros: { codigo?: string; nombre?: string; profesor?: string; ciclo?: string }): Observable<Curso[]> {
    let params = new HttpParams();
    Object.entries(filtros).forEach(([clave, valor]) => {
      if (valor) params = params.set(clave, valor);
    });
    return this.http.get<Curso[]>(`${this.apiUrl}/buscar`, { params });
  }

  crear(curso: CursoRequest): Observable<Curso> {
    return this.http.post<Curso>(this.apiUrl, curso);
  }

  actualizar(id: number, curso: CursoRequest): Observable<Curso> {
    return this.http.put<Curso>(`${this.apiUrl}/${id}`, curso);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
