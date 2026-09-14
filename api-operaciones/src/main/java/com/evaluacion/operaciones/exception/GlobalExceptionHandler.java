package com.evaluacion.operaciones.exception;

import com.evaluacion.operaciones.dto.ErrorResponse;
import feign.FeignException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validarRequest(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        String mensaje = errores.values()
                .stream()
                .findFirst()
                .orElse("La petición contiene datos inválidos");

        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        mensaje,
                        errores
                )
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> argumentoInvalido(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                Instant.now(), 400, ex.getMessage(), Map.of()
        ));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> errorFeign(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now(), status.value(), "Error al comunicarse con API de transacciones", Map.of()
        ));
    }
}
