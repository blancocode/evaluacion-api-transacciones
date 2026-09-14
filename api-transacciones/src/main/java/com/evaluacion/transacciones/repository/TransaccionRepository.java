package com.evaluacion.transacciones.repository;

import com.evaluacion.transacciones.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    boolean existsByReferencia(String referencia);

    @Modifying
    @Query("""
            update Transaccion t
               set t.estatus = 'Cancelada'
             where t.id = :id
               and t.referencia = :referencia
               and t.estatus = 'Aprobada'
            """)
    int cancelarTransaccion(@Param("id") Long id, @Param("referencia") String referencia);
}
