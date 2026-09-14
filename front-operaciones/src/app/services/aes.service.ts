import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AesService {

  async cifrar(texto: string): Promise<string> {
    const keyBytes = this.base64ToBytes(environment.aesKeyBase64);
    const key = await crypto.subtle.importKey(
      'raw', keyBytes, { name: 'AES-GCM' }, false, ['encrypt']
    );

    const iv = crypto.getRandomValues(new Uint8Array(12));
    const textoBytes = new TextEncoder().encode(texto);
    const encrypted = await crypto.subtle.encrypt({ name: 'AES-GCM', iv }, key, textoBytes);

    const encryptedBytes = new Uint8Array(encrypted);
    const resultado = new Uint8Array(iv.length + encryptedBytes.length);
    resultado.set(iv, 0);
    resultado.set(encryptedBytes, iv.length);

    return this.bytesToBase64(resultado);
  }

  private base64ToBytes(base64: string): Uint8Array {
    const binary = atob(base64);
    return Uint8Array.from(binary, c => c.charCodeAt(0));
  }

  private bytesToBase64(bytes: Uint8Array): string {
    let binary = '';
    bytes.forEach(byte => binary += String.fromCharCode(byte));
    return btoa(binary);
  }
}
