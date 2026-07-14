import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AsistenciaService } from '../../core/services/asistencia.service';
import { Asistencia } from '../../core/models/asistencia.model';
import { CursoService } from '../../core/services/curso.service';
import { Curso } from '../../core/models/curso.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-asistencia-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './asistencia-list.component.html'
})
export class AsistenciaListComponent implements OnInit {
  asistencias = signal<Asistencia[]>([]);
  cursos = signal<Curso[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);

  private fb = inject(FormBuilder);

  filtroForm = this.fb.group({
    fecha: [''],
    estado: [''],
    cursoId: [null as number | null]
  });

  constructor(
    private asistenciaService: AsistenciaService,
    private cursoService: CursoService,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.cursoService.listar().subscribe({ next: (cursos) => this.cursos.set(cursos) });
  }

  cargar(): void {
    this.cargando.set(true);
    this.asistenciaService.listar().subscribe({
      next: (asistencias) => {
        this.asistencias.set(asistencias);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar las asistencias.');
        this.cargando.set(false);
      }
    });
  }

  filtrar(): void {
    const { fecha, estado, cursoId } = this.filtroForm.getRawValue();
    this.cargando.set(true);
    this.asistenciaService
      .buscar({
        fecha: fecha || undefined,
        estado: estado || undefined,
        cursoId: cursoId || undefined
      })
      .subscribe({
        next: (asistencias) => {
          this.asistencias.set(asistencias);
          this.cargando.set(false);
        },
        error: () => {
          this.error.set('No se pudo completar la búsqueda.');
          this.cargando.set(false);
        }
      });
  }

  limpiarFiltros(): void {
    this.filtroForm.reset({ fecha: '', estado: '', cursoId: null });
    this.cargar();
  }

  claseSello(estado: string): string {
    const normalizado = (estado || '').toLowerCase();
    if (normalizado === 'presente') return 'sello sello-presente';
    if (normalizado === 'tarde') return 'sello sello-tarde';
    return 'sello sello-falta';
  }

  eliminar(asistencia: Asistencia): void {
    if (!asistencia.id) return;
    if (!confirm('¿Eliminar este registro de asistencia?')) return;

    this.asistenciaService.eliminar(asistencia.id).subscribe({
      next: () => this.asistencias.update((lista) => lista.filter((a) => a.id !== asistencia.id)),
      error: () => this.error.set('No se pudo eliminar el registro.')
    });
  }
}
