import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },

  // ---- Cursos (ADMIN gestiona, DOCENTE consulta) ----
  {
    path: 'cursos',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    loadComponent: () =>
      import('./features/cursos/curso-list.component').then((m) => m.CursoListComponent)
  },
  {
    path: 'cursos/nuevo',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./features/cursos/curso-form.component').then((m) => m.CursoFormComponent)
  },
  {
    path: 'cursos/:id/editar',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./features/cursos/curso-form.component').then((m) => m.CursoFormComponent)
  },

  // ---- Estudiantes (ADMIN gestiona, DOCENTE consulta) ----
  {
    path: 'estudiantes',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    loadComponent: () =>
      import('./features/estudiantes/estudiante-list.component').then((m) => m.EstudianteListComponent)
  },
  {
    path: 'estudiantes/nuevo',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./features/estudiantes/estudiante-form.component').then((m) => m.EstudianteFormComponent)
  },
  {
    path: 'estudiantes/:id/editar',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./features/estudiantes/estudiante-form.component').then((m) => m.EstudianteFormComponent)
  },

  // ---- Asistencias (ADMIN y DOCENTE gestionan) ----
  {
    path: 'asistencias',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    loadComponent: () =>
      import('./features/asistencias/asistencia-list.component').then((m) => m.AsistenciaListComponent)
  },
  {
    path: 'asistencias/nueva',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    loadComponent: () =>
      import('./features/asistencias/asistencia-form.component').then((m) => m.AsistenciaFormComponent)
  },
  {
    path: 'asistencias/:id/editar',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    loadComponent: () =>
      import('./features/asistencias/asistencia-form.component').then((m) => m.AsistenciaFormComponent)
  },

  // ---- Vista exclusiva del ALUMNO ----
  {
    path: 'mi-asistencia',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ALUMNO'] },
    loadComponent: () =>
      import('./features/asistencias/mi-asistencia.component').then((m) => m.MiAsistenciaComponent)
  },

  // ---- Docentes (solo ADMIN) ----
  {
    path: 'docentes',
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () =>
      import('./features/docentes/docente-list.component').then((m) => m.DocenteListComponent)
  },

  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: '**', redirectTo: 'dashboard' }
];
