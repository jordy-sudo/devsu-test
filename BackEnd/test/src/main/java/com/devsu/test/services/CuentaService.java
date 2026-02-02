package com.devsu.test.services;

import com.devsu.test.domain.dto.request.CuentaCreateRequest;
import com.devsu.test.domain.dto.response.CuentaResponse;
import com.devsu.test.domain.entities.Cliente;
import com.devsu.test.domain.entities.Cuenta;
import com.devsu.test.domain.enums.TipoCuenta;
import com.devsu.test.exceptions.BusinessException;
import com.devsu.test.repositories.ClienteRepository;
import com.devsu.test.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public CuentaResponse create(CuentaCreateRequest req) {

        if (cuentaRepository.existsByNumeroCuenta(req.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con ese número");
        }

        Cliente cliente = clienteRepository.findById(req.getClienteId())
                .orElseThrow(() -> new BusinessException("Cliente no existe"));

        if (!Boolean.TRUE.equals(cliente.getEstado())) {
            throw new BusinessException("Cliente inactivo, no puede crear cuentas");
        }

        TipoCuenta tipo;
        try {
            tipo = TipoCuenta.valueOf(req.getTipoCuenta().trim().toUpperCase());
        } catch (Exception e) {
            throw new BusinessException("tipoCuenta inválido. Use: AHORROS o CORRIENTE");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(req.getNumeroCuenta());
        cuenta.setTipoCuenta(tipo);
        cuenta.setSaldoInicial(req.getSaldoInicial());
        cuenta.setEstado(req.getEstado() != null ? req.getEstado() : true);
        cuenta.setCliente(cliente);

        Cuenta saved = cuentaRepository.save(cuenta);

        return new CuentaResponse(
                saved.getNumeroCuenta(),
                saved.getTipoCuenta().name(),
                saved.getSaldoInicial(),
                saved.getEstado(),
                saved.getCliente().getId()
        );
    }
}
