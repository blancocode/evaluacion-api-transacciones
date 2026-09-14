import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ApiService } from './services/api.service';
import { AesService } from './services/aes.service';
import { TransaccionResponse } from './models/models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  autenticado = false;
  mensaje = '';
  error = '';
  transacciones: TransaccionResponse[] = [];

  loginForm = this.fb.group({
    usuario: ['', Validators.required],
    password: ['', Validators.required]
  });

  paginaActual = 0;
  registrosPorPagina = 5;
  totalPaginas = 0;
  totalRegistros = 0;
  ultimaPagina = false;

  operacionForm = this.fb.group({
    operacion: ['', Validators.required],
    importe: ['', Validators.required],
    cliente: ['', Validators.required],
    secreto: ['', Validators.required]
  });

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private aesService: AesService
  ) {}

  login(): void {
    this.limpiarMensajes();
    if (this.loginForm.invalid) {
      this.error = 'Captura usuario y password.';
      return;
    }

    const { usuario, password } = this.loginForm.getRawValue();
    this.apiService.login(usuario!, password!).subscribe({
      next: response => {
        this.autenticado = response.valido;
        response.valido ? this.cargarTransacciones() : this.error = response.mensaje;
      },
      error: () => this.error = 'No fue posible iniciar sesión.'
    });
  }

  async registrar(): Promise<void> {
    this.limpiarMensajes();
    console.log('Formulario válido:', this.operacionForm.valid);

  console.log(
    'operacion:',
    this.operacionForm.get('operacion')?.errors
  );

  console.log(
    'importe:',
    this.operacionForm.get('importe')?.errors
  );

  console.log(
    'cliente:',
    this.operacionForm.get('cliente')?.errors
  );

  console.log(
    'secreto:',
    this.operacionForm.get('secreto')?.errors
  );
    if (this.operacionForm.invalid) {
      this.operacionForm.markAllAsTouched();
      this.error = 'Revisa los campos de la operación.';
      return;
    }

    const value = this.operacionForm.getRawValue();
    const secretoCifrado = await this.aesService.cifrar(value.secreto!);

    this.apiService.registrar({
      operacion: value.operacion!,
      importe: value.importe!,
      cliente: value.cliente!,
      secreto: secretoCifrado
    }).subscribe({
      next: response => {
        this.mensaje = `Operación registrada. Referencia: ${response.referencia}`;
        this.operacionForm.reset();
        this.cargarTransacciones();
      },
      error: error => this.error = error?.error?.mensaje ?? 'No fue posible registrar la operación.'
    });
  }

  cargarTransacciones(): void {

    this.apiService.consultar(
      this.paginaActual,
      this.registrosPorPagina
    ).subscribe({
  
      next: response => {
  
        console.log('Respuesta paginada:', response);
  
        this.transacciones = response.contenido;
        this.paginaActual = response.pagina;
        this.registrosPorPagina = response.registrosPorPagina;
        this.totalRegistros = response.totalRegistros;
        this.totalPaginas = response.totalPaginas;
        this.ultimaPagina = response.ultimaPagina;
  
      },
  
      error: error => {
  
        console.error('Error al consultar transacciones:', error);
  
        this.transacciones = [];
      }
  
    });
  
  }

  paginaAnterior(): void {

    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.cargarTransacciones();
    }
  
  }

  paginaSiguiente(): void {

    if (!this.ultimaPagina) {
      this.paginaActual++;
      this.cargarTransacciones();
    }
  
  }

  private limpiarMensajes(): void {
    this.mensaje = '';
    this.error = '';
  }
}
