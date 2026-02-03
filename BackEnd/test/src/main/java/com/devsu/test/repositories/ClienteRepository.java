package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByPersona_Identificacion(String identificacion);

    Optional<Cliente> findByPersona_Identificacion(String identificacion);
}
