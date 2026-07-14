export interface DocenteResponse {
  id: number;
  username: string;
  nombreCompleto?: string;
  rol: string;
}

// DTO que espera el backend en POST /api/docentes (DocenteRequest.java)
export interface DocenteRequest {
  username: string;
  password: string;
  nombreCompleto?: string;
}
