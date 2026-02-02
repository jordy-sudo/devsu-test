package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.CuentaCreateRequest;
import com.devsu.test.domain.dto.response.CuentaResponse;
import com.devsu.test.services.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse create(@Valid @RequestBody CuentaCreateRequest req) {
        return cuentaService.create(req);
    }
}
