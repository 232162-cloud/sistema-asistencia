import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EstudianteService } from '../../core/services/estudiante.service';
import { Estudiante } from '../../core/models/estudiante.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-estudiante-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './estudiante-list.component.html'
})
export class EstudianteListComponent implements OnInit {
  estudiantes = signal<Estudiante[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);
  busqueda = signal('');

  constructor(private estudianteService: EstudianteService, public auth: AuthService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    this.estudianteService.listar().subscribe({
      next: (estudiantes) => {
        this.estudiantes.set(estudiantes);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los estudiantes.');
        this.cargando.set(false);
      }
    });
  }

  buscar(termino: string): void {
    this.busqueda.set(termino);
    if (!termino.trim()) {
      this.cargar();
      return;
    }
    this.estudianteService.buscar({ nombres: termino }).subscribe({
      next: (estudiantes) => this.estudiantes.set(estudiantes),
      error: () => this.error.set('No se pudo completar la búsqueda.')
    });
  }

  eliminar(estudiante: Estudiante): void {
    if (!estudiante.id) return;
    if (!confirm(`¿Eliminar a ${estudiante.nombres} ${estudiante.apellidos}?`)) return;

    this.estudianteService.eliminar(estudiante.id).subscribe({
      next: () => this.estudiantes.update((lista) => lista.filter((e) => e.id !== estudiante.id)),
      error: () => this.error.set('No se pudo eliminar el estudiante.')
    });
  }
}
