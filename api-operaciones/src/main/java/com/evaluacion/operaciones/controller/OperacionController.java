package com.evaluacion.operaciones.controller;

import com.evaluacion.operaciones.dto.*;
import com.evaluacion.operaciones.service.OperacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Operaciones", description = "Servicios disponibles para el front")
public class OperacionController {

    private final OperacionService operacionService;

    public OperacionController(OperacionService operacionService) {
        this.operacionService = operacionService;
    }

    @Operation(summary = "Registrar una operación")
    @PostMapping("/operaciones")
    public TransaccionResponse registrar(@Valid @RequestBody OperacionRequest request) {
        return operacionService.procesar(request);
    }

    @Operation(summary = "Validar usuario y contraseña")
    @PostMapping("/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return operacionService.login(request);
    }

    @Operation(summary = "Cancelar una transacción aprobada")
    @PatchMapping("/transacciones/estatus")
    public TransaccionResponse cancelar(@Valid @RequestBody CancelarRequest request) {
        return operacionService.cancelar(request);
    }

    @Operation(summary = "Consultar transacciones con paginación")
    @GetMapping("/transacciones")
    public PaginaResponse<TransaccionResponse> consultar(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return operacionService.consultar(page, size, sortBy, direction);
    }
}
