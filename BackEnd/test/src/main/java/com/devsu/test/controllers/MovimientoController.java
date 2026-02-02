package com.devsu.test.controllers;

import com.devsu.test.domain.dto.request.MovimientoCreateRequest;
import com.devsu.test.domain.dto.response.MovimientoListadoResponse;
import com.devsu.test.domain.dto.response.MovimientoResponse;
import com.devsu.test.services.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse crear(@Valid @RequestBody MovimientoCreateRequest req) {
        return movimientoService.crear(req);
    }

    @GetMapping
    public List<MovimientoListadoResponse> listar(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        return movimientoService.listarPorClienteYRango(clienteId, desde, hasta);
    }
}
