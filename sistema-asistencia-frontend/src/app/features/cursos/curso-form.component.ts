import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CursoService } from '../../core/services/curso.service';
import { DocenteService } from '../../core/services/docente.service';
import { DocenteResponse } from '../../core/models/docente.model';
import { CursoRequest } from '../../core/models/curso.model';

@Component({
  selector: 'app-curso-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './curso-form.component.html'
})
export class CursoFormComponent implements OnInit {
  docentes = signal<DocenteResponse[]>([]);
  cargando = signal(false);
  error = signal<string | null>(null);
  cursoId = signal<number | null>(null);
  esEdicion = signal(false);

  private fb = inject(FormBuilder);

  form = this.fb.group({
    codigo: ['', Validators.required],
    nombre: ['', Validators.required],
    descripcion: [''],
    creditos: [null as number | null],
    profesor: [''],
    ciclo: [''],
    docenteId: [null as number | null]
  });

  constructor(
    private cursoService: CursoService,
    private docenteService: DocenteService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.docenteService.listar().subscribe({
      next: (docentes) => this.docentes.set(docentes),
      error: () => this.error.set('No se pudo cargar la lista de docentes.')
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.cursoId.set(id);
      this.esEdicion.set(true);
      this.cursoService.obtenerPorId(id).subscribe({
        next: (curso) =>
          this.form.patchValue({
            codigo: curso.codigo,
            nombre: curso.nombre,
            descripcion: curso.descripcion,
            creditos: curso.creditos ?? null,
            profesor: curso.profesor,
            ciclo: curso.ciclo,
            docenteId: curso.docente?.id ?? null
          }),
        error: () => this.error.set('No se pudo cargar el curso.')
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

    const payload = this.form.getRawValue() as CursoRequest;
    const peticion = this.esEdicion()
      ? this.cursoService.actualizar(this.cursoId()!, payload)
      : this.cursoService.crear(payload);

    peticion.subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/cursos']);
      },
      error: (err) => {
        this.cargando.set(false);
        this.error.set(typeof err.error === 'string' ? err.error : 'No se pudo guardar el curso.');
      }
    });
  }
}
