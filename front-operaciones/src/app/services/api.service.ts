import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginResponse, PaginaResponse, TransaccionResponse } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ApiService {

  private readonly api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(usuario: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.api}/auth/login`, { usuario, password });
  }

  registrar(request: { operacion: string; importe: string; cliente: string; secreto: string }): Observable<TransaccionResponse> {
    return this.http.post<TransaccionResponse>(`${this.api}/operaciones`, request);
  }

  consultar(page: number, size: number): Observable<PaginaResponse<TransaccionResponse>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', 'id')
      .set('direction', 'desc');

    return this.http.get<PaginaResponse<TransaccionResponse>>(`${this.api}/transacciones`, { params });
  }
}
