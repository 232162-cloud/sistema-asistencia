import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AsistenciaService } from '../../core/services/asistencia.service';
import { EstudianteService } from '../../core/services/estudiante.service';
import { CursoService } from '../../core/services/curso.service';
import { Estudiante } from '../../core/models/estudiante.model';
import { Curso } from '../../core/models/curso.model';
import { AsistenciaRequest } from '../../core/models/asistencia.model';

@Component({
  selector: 'app-asistencia-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './asistencia-form.component.html'
})
export class AsistenciaFormComponent implements OnInit {
  estudiantes = signal<Estudiante[]>([]);
  cursos = signal<Curso[]>([]);
  cargando = signal(false);
  error = signal<string | null>(null);
  asistenciaId = signal<number | null>(null);
  esEdicion = signal(false);

  private fb = inject(FormBuilder);

  form = this.fb.group({
    estudianteId: [null as number | null, Validators.required],
    cursoId: [null as number | null, Validators.required],
    fecha: ['', Validators.required],
    estado: ['Presente', Validators.required],
    observaciones: ['']
  });

  constructor(
    private asistenciaService: AsistenciaService,
    private estudianteService: EstudianteService,
    private cursoService: CursoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.estudianteService.listar().subscribe({ next: (e) => this.estudiantes.set(e) });
    this.cursoService.listar().subscribe({ next: (c) => this.cursos.set(c) });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.asistenciaId.set(id);
      this.esEdicion.set(true);
      this.asistenciaService.obtenerPorId(id).subscribe({
        next: (asistencia) =>
          this.form.patchValue({
            estudianteId: asistencia.estudiante?.id ?? null,
            cursoId: asistencia.curso?.id ?? null,
            fecha: asistencia.fecha,
            estado: asistencia.estado as string,
            observaciones: asistencia.observaciones
          }),
        error: () => this.error.set('No se pudo cargar el registro de asistencia.')
      });
    }
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando.set(true);
    this.error.set(null);

    const payload = this.form.getRawValue() as AsistenciaRequest;
    const peticion = this.esEdicion()
      ? this.asistenciaService.actualizar(this.asistenciaId()!, payload)
      : this.asistenciaService.crear(payload);

    peticion.subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/asistencias']);
      },
      error: (err) => {
        this.cargando.set(false);
        this.error.set(
          err.status === 403
            ? 'No tienes permisos para registrar asistencia en este curso.'
            : 'No se pudo guardar el registro.'
        );
      }
    });
  }
}
