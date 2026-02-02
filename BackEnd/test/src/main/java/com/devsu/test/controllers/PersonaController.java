package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.PersonaCreateRequest;
import com.devsu.test.domain.dto.response.PersonaResponse;
import com.devsu.test.services.PersonaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonaResponse create(@Valid @RequestBody PersonaCreateRequest request) {
        return personaService.create(request);
    }
}
