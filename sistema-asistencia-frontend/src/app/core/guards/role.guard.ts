import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Rol } from '../models/auth.model';

// Uso en rutas: { path: 'cursos', canActivate: [authGuard, roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } }
export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const rolesPermitidos = route.data['roles'] as Rol[] | undefined;

  if (!rolesPermitidos || rolesPermitidos.length === 0) {
    return true;
  }
  if (authService.tieneRol(...rolesPermitidos)) {
    return true;
  }
  router.navigate(['/dashboard']);
  return false;
};
