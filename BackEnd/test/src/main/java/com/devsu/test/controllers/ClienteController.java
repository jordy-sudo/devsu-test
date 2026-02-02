package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.ClienteCreateRequest;
import com.devsu.test.domain.dto.response.ClienteResponse;
import com.devsu.test.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse create(@Valid @RequestBody ClienteCreateRequest req) {
        return clienteService.createClienteFlujo1(req);
    }
}
