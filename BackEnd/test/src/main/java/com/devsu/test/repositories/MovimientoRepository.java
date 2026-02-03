package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    // Último movimiento (para saldo actual)
    Optional<Movimiento> findTopByCuenta_NumeroCuentaOrderByIdDesc(String numeroCuenta);

    // Suma retiros del día (límite diario)
    @Query("""
        SELECT COALESCE(SUM(ABS(m.valor)), 0)
        FROM Movimiento m
        WHERE m.cuenta.numeroCuenta = :numeroCuenta
          AND m.fecha = :fecha
          AND m.valor < 0
          AND m.estado = true
    """)
    BigDecimal sumRetirosDelDia(String numeroCuenta, LocalDate fecha);

    // Listado por cliente + rango + búsqueda rápida (q)
    @Query("""
        SELECT m
        FROM Movimiento m
        WHERE m.cuenta.cliente.id = :clienteId
          AND m.fecha BETWEEN :desde AND :hasta
          AND m.estado = true
          AND (
                :q IS NULL OR :q = '' OR
                LOWER(m.cuenta.numeroCuenta) LIKE CONCAT('%', LOWER(:q), '%') OR
                LOWER(m.cuenta.tipoCuenta) LIKE CONCAT('%', LOWER(:q), '%') OR
                LOWER(m.tipoMovimiento) LIKE CONCAT('%', LOWER(:q), '%') OR
                LOWER(m.cuenta.cliente.persona.nombre) LIKE CONCAT('%', LOWER(:q), '%') OR
                LOWER(m.cuenta.cliente.persona.identificacion) LIKE CONCAT('%', LOWER(:q), '%')
          )
        ORDER BY m.fecha ASC, m.id ASC
    """)
    List<Movimiento> findByClienteRangoYQuery(Long clienteId, LocalDate desde, LocalDate hasta, String q);
}
