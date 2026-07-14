import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { AsistenciaService } from '../../core/services/asistencia.service';
import { Asistencia } from '../../core/models/asistencia.model';

@Component({
  selector: 'app-mi-asistencia',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mi-asistencia.component.html'
})
export class MiAsistenciaComponent implements OnInit {
  asistencias = signal<Asistencia[]>([]);
  cargando = signal(true);
  error = signal<string | null>(null);

  resumen = computed(() => {
    const lista = this.asistencias();
    const total = lista.length;
    const presente = lista.filter((a) => a.estado === 'Presente').length;
    const tarde = lista.filter((a) => a.estado === 'Tarde').length;
    const falta = lista.filter((a) => a.estado === 'Falta').length;
    return { total, presente, tarde, falta };
  });

  constructor(private asistenciaService: AsistenciaService) {}

  ngOnInit(): void {
    this.asistenciaService.miAsistencia().subscribe({
      next: (asistencias) => {
        this.asistencias.set(asistencias);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar tu historial de asistencia.');
        this.cargando.set(false);
      }
    });
  }

  claseSello(estado: string): string {
    const normalizado = (estado || '').toLowerCase();
    if (normalizado === 'presente') return 'sello sello-presente';
    if (normalizado === 'tarde') return 'sello sello-tarde';
    return 'sello sello-falta';
  }
}
