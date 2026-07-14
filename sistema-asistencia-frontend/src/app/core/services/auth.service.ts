import { HttpClient } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, Rol } from '../models/auth.model';

const TOKEN_KEY = 'asistencia_token';
const USERNAME_KEY = 'asistencia_username';
const ROL_KEY = 'asistencia_rol';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/auth`;

  // Estado reactivo de sesión, inicializado desde localStorage al recargar la página
  readonly username = signal<string | null>(localStorage.getItem(USERNAME_KEY));
  readonly rol = signal<Rol | null>(localStorage.getItem(ROL_KEY) as Rol | null);
  readonly isLoggedIn = computed(() => !!this.username());

  constructor(private http: HttpClient) {}

  login(credenciales: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.apiUrl, credenciales).pipe(
      tap((res) => this.guardarSesion(res))
    );
  }

  refrescarToken(): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, {}).pipe(
      tap((res) => this.guardarSesion(res))
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    localStorage.removeItem(ROL_KEY);
    this.username.set(null);
    this.rol.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  tieneRol(...roles: Rol[]): boolean {
    const actual = this.rol();
    return !!actual && roles.includes(actual);
  }

  private guardarSesion(res: AuthResponse): void {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USERNAME_KEY, res.username);
    localStorage.setItem(ROL_KEY, res.rol);
    this.username.set(res.username);
    this.rol.set(res.rol);
  }
}
