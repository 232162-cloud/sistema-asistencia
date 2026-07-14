import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DocenteService } from '../../core/services/docente.service';
import { DocenteResponse } from '../../core/models/docente.model';

@Component({
  selector: 'app-docente-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './docente-list.component.html'
})
export class DocenteListComponent implements OnInit {
  docentes = signal<DocenteResponse[]>([]);
  cargando = signal(true);
  guardando = signal(false);
  error = signal<string | null>(null);
  mostrarFormulario = signal(false);

  private fb = inject(FormBuilder);

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6)]],
    nombreCompleto: ['']
  });

  constructor(private docenteService: DocenteService) {}

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    this.docenteService.listar().subscribe({
      next: (docentes) => {
        this.docentes.set(docentes);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudieron cargar los docentes.');
        this.cargando.set(false);
      }
    });
  }

  crear(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando.set(true);
    this.error.set(null);

    this.docenteService.crear(this.form.getRawValue() as any).subscribe({
      next: (docente) => {
        this.docentes.update((lista) => [...lista, docente]);
        this.guardando.set(false);
        this.mostrarFormulario.set(false);
        this.form.reset();
      },
      error: (err) => {
        this.guardando.set(false);
        this.error.set(
          err.status === 409 ? 'Ese nombre de usuario ya existe.' : 'No se pudo crear el docente.'
        );
      }
    });
  }
}
