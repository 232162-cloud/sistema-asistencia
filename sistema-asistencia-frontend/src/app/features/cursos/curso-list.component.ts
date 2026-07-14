import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CursoService } from '../../core/services/curso.service';
import { Curso } from '../../core/models/curso.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-curso-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './curso-list.component.html'
})
export class CursoListComponent implements OnInit {
  cursos = signal<Curso[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);

  constructor(private cursoService: CursoService, public auth: AuthService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    this.cursoService.listar().subscribe({
      next: (cursos) => {
        this.cursos.set(cursos);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los cursos.');
        this.cargando.set(false);
      }
    });
  }

  eliminar(curso: Curso): void {
    if (!curso.id) return;
    if (!confirm(`¿Eliminar el curso "${curso.nombre}"? Esta acción no se puede deshacer.`)) return;

    this.cursoService.eliminar(curso.id).subscribe({
      next: () => this.cursos.update((lista) => lista.filter((c) => c.id !== curso.id)),
      error: () => this.error.set('No se pudo eliminar el curso.')
    });
  }
}
