package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // opcional, por si quieres validar por relación
    // boolean existsByPersona_Identificacion(String identificacion);
}
