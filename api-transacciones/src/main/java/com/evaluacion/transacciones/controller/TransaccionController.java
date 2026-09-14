package com.evaluacion.transacciones.controller;

import com.evaluacion.transacciones.dto.*;
import com.evaluacion.transacciones.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interno/transacciones")
@Tag(name = "Transacciones internas")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @Operation(summary = "Guardar una transacción")
    @PostMapping
    public TransaccionResponse guardar(@RequestBody TransaccionRequest request) {
        return transaccionService.guardar(request);
    }

    @Operation(summary = "Cancelar una transacción")
    @PatchMapping("/estatus")
    public TransaccionResponse cancelar(@RequestBody CancelarRequest request) {
        return transaccionService.cancelar(request);
    }

    @Operation(summary = "Consultar transacciones paginadas")
    @GetMapping
    public PaginaResponse<TransaccionResponse> consultar(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction) {
        return transaccionService.consultar(page, size, sortBy, direction);
    }
}
