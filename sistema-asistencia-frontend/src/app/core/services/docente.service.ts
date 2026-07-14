import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { DocenteRequest, DocenteResponse } from '../models/docente.model';

@Injectable({ providedIn: 'root' })
export class DocenteService {
  private readonly apiUrl = `${environment.apiUrl}/docentes`;

  constructor(private http: HttpClient) {}

  listar(): Observable<DocenteResponse[]> {
    return this.http.get<DocenteResponse[]>(this.apiUrl);
  }

  crear(docente: DocenteRequest): Observable<DocenteResponse> {
    return this.http.post<DocenteResponse>(this.apiUrl, docente);
  }
}
