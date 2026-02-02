package com.devsu.test.services;

import com.devsu.test.domain.dto.request.PersonaCreateRequest;
import com.devsu.test.domain.dto.response.PersonaResponse;
import com.devsu.test.domain.entities.Persona;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Transactional
    public PersonaResponse create(PersonaCreateRequest req) {

        if (personaRepository.existsByIdentificacion(req.getIdentificacion())) {
            throw new BusinessException("Ya existe una persona con esa identificación");
        }

        Persona p = new Persona();
        p.setNombre(req.getNombre());
        p.setGenero(req.getGenero());
        p.setEdad(req.getEdad());
        p.setIdentificacion(req.getIdentificacion());
        p.setDireccion(req.getDireccion());
        p.setTelefono(req.getTelefono());

        Persona saved = personaRepository.save(p);

        return new PersonaResponse(
                saved.getId(),
                saved.getNombre(),
                saved.getGenero(),
                saved.getEdad(),
                saved.getIdentificacion(),
                saved.getDireccion(),
                saved.getTelefono()
        );
    }
}
