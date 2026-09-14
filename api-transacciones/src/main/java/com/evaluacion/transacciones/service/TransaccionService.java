package com.evaluacion.transacciones.service;

import com.evaluacion.transacciones.dto.*;
import com.evaluacion.transacciones.entity.Transaccion;
import com.evaluacion.transacciones.repository.TransaccionRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Set;

@Service
public class TransaccionService {

    private static final String APROBADA = "Aprobada";
    private static final Set<String> CAMPOS_ORDEN = Set.of("id", "operacion", "importe", "cliente",         "referencia", "estatus");

    private final TransaccionRepository repository;
    private final SecureRandom random = new SecureRandom();

    public TransaccionService(TransaccionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TransaccionResponse guardar(TransaccionRequest request) {
        Transaccion transaccion = new Transaccion();
        transaccion.setOperacion(request.operacion());
        transaccion.setImporte(request.importe());
        transaccion.setCliente(request.cliente());
        transaccion.setReferencia(generarReferencia());
        transaccion.setEstatus(APROBADA);
        transaccion.setSecreto("secreto");

        return toResponse(repository.save(transaccion));
    }

    @Transactional
    public TransaccionResponse cancelar(CancelarRequest request) {
        if (request.estatus() == null || !"cancelar".equalsIgnoreCase(request.estatus())) {
            throw new IllegalArgumentException("El estatus debe contener el valor cancelar");
        }

        int actualizados = repository.cancelarTransaccion(request.id(), request.referencia());
        if (actualizados == 0) {
            throw new IllegalArgumentException("No se encontró una transacción con el id y referencia enviados");
        }

        return repository.findById(request.id())
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la transacción"));
    }

    @Transactional(readOnly = true)
    public PaginaResponse<TransaccionResponse> consultar(int page, int size, 
        String sortBy, String direction) {
        if (!CAMPOS_ORDEN.contains(sortBy)) {
            throw new IllegalArgumentException("Campo de ordenamiento no válido");
        }

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Transaccion> resultado = repository.findAll(pageable);

        return new PaginaResponse<>(
                resultado.getContent().stream().map(this::toResponse).toList(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                resultado.isLast()
        );
    }

    private String generarReferencia() {
        String referencia;
        do {
            referencia = String.valueOf(100000 + random.nextInt(900000));
        } while (repository.existsByReferencia(referencia));
        return referencia;
    }

    private TransaccionResponse toResponse(Transaccion t) {
        return new TransaccionResponse(
                t.getId(),
                t.getEstatus(),
                t.getReferencia(),
                t.getOperacion(),
                t.getImporte(),
                t.getCliente()
        );
    }
}
