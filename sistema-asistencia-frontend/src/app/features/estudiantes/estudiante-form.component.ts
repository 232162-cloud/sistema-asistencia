import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EstudianteService } from '../../core/services/estudiante.service';
import { Estudiante } from '../../core/models/estudiante.model';

@Component({
  selector: 'app-estudiante-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './estudiante-form.component.html'
})
export class EstudianteFormComponent implements OnInit {
  cargando = signal(false);
  error = signal<string | null>(null);
  estudianteId = signal<number | null>(null);
  esEdicion = signal(false);

  private fb = inject(FormBuilder);

  form = this.fb.group({
    codigo: ['', Validators.required],
    nombres: ['', Validators.required],
    apellidos: ['', Validators.required],
    carrera: ['']
  });

  constructor(
    private estudianteService: EstudianteService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      const id = Number(idParam);
      this.estudianteId.set(id);
      this.esEdicion.set(true);
      this.estudianteService.obtenerPorId(id).subscribe({
        next: (estudiante) =>
          this.form.patchValue({
            codigo: estudiante.codigo,
            nombres: estudiante.nombres,
            apellidos: estudiante.apellidos,
            carrera: estudiante.carrera
          }),
        error: () => this.error.set('No se pudo cargar el estudiante.')
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

    const payload = this.form.getRawValue() as Estudiante;
    const peticion = this.esEdicion()
      ? this.estudianteService.actualizar(this.estudianteId()!, payload)
      : this.estudianteService.crear(payload);

    peticion.subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/estudiantes']);
      },
      error: () => {
        this.cargando.set(false);
        this.error.set('No se pudo guardar el estudiante.');
      }
    });
  }
}
