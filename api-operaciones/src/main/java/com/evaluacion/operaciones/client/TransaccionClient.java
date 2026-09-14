package com.evaluacion.operaciones.client;

import com.evaluacion.operaciones.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "api-transacciones", url = "${services.transacciones.url}")
public interface TransaccionClient {

    @PostMapping("/interno/transacciones")
    TransaccionResponse guardar(@RequestBody TransaccionRequest request);

    @PostMapping("/interno/auth/login")
    LoginResponse login(@RequestBody LoginRequest request);

    @PatchMapping("/interno/transacciones/estatus")
    TransaccionResponse cancelar(@RequestBody CancelarRequest request);

    @GetMapping("/interno/transacciones")
    PaginaResponse<TransaccionResponse> consultar(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction);
}
