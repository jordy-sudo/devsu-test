package com.devsu.test.repositories;

import com.devsu.test.domain.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByIdentificacion(String identificacion);

    boolean existsByIdentificacion(String identificacion);
}
