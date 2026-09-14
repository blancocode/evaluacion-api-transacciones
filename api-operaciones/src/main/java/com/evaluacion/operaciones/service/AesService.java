package com.evaluacion.operaciones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AesService {

    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;
    private final SecretKeySpec secretKey;

    public AesService(@Value("${security.aes.key-base64}") String keyBase64) {
        byte[] key = Base64.getDecoder().decode(keyBase64);
        if (key.length != 32) {
            throw new IllegalArgumentException("La llave AES debe ser de 256 bits");
        }
        this.secretKey = new SecretKeySpec(key, "AES");
    }

    public String descifrar(String textoCifrado) {
        try {
            byte[] entrada = Base64.getDecoder().decode(textoCifrado);
            if (entrada.length <= IV_LENGTH) {
                throw new IllegalArgumentException("El secreto cifrado no tiene un formato válido");
            }

            byte[] iv = Arrays.copyOfRange(entrada, 0, IV_LENGTH);
            byte[] cipherText = Arrays.copyOfRange(entrada, IV_LENGTH, entrada.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH, iv));

            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("No fue posible descifrar el secreto");
        }
    }
}
