package com.evaluacion.operaciones.service;

import com.evaluacion.operaciones.client.TransaccionClient;
import com.evaluacion.operaciones.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OperacionService {

    private final AesService aesService;
    private final TransaccionClient transaccionClient;

    public OperacionService(AesService aesService, TransaccionClient transaccionClient) {
        this.aesService = aesService;
        this.transaccionClient = transaccionClient;
    }

    public TransaccionResponse procesar(OperacionRequest request) {
        String secretoDescifrado = aesService.descifrar(request.secreto());

        TransaccionRequest transaccion = new TransaccionRequest(
                request.operacion(),
                new BigDecimal(request.importe()),
                request.cliente(),
                secretoDescifrado
        );

        return transaccionClient.guardar(transaccion);
    }

    public LoginResponse login(LoginRequest request) {
        return transaccionClient.login(request);
    }

    public TransaccionResponse cancelar(CancelarRequest request) {
        return transaccionClient.cancelar(request);
    }

    public PaginaResponse<TransaccionResponse> consultar(int page, int size, String sortBy, String direction) {
        return transaccionClient.consultar(page, size, sortBy, direction);
    }
}
