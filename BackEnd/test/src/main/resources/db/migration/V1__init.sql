-- 1) PERSONA
CREATE TABLE IF NOT EXISTS persona (
                                       id BIGSERIAL PRIMARY KEY,
                                       nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INT NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(30) NOT NULL
    );

-- 2) CLIENTE (PK = FK a PERSONA)
CREATE TABLE IF NOT EXISTS cliente (
                                       cliente_id BIGINT PRIMARY KEY,
                                       contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_cliente_persona
    FOREIGN KEY (cliente_id)
    REFERENCES persona(id)
    );

-- 3) CUENTA
CREATE TABLE IF NOT EXISTS cuenta (
                                      numero_cuenta VARCHAR(30) PRIMARY KEY,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial NUMERIC(15,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_cuenta_cliente
    FOREIGN KEY (cliente_id)
    REFERENCES cliente(cliente_id)
    );

-- 4) MOVIMIENTO
CREATE TABLE IF NOT EXISTS movimiento (
                                          id BIGSERIAL PRIMARY KEY,
                                          fecha DATE NOT NULL,
                                          tipo_movimiento VARCHAR(20) NOT NULL,
    valor NUMERIC(15,2) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL,
    numero_cuenta VARCHAR(30) NOT NULL,
    CONSTRAINT fk_movimiento_cuenta
    FOREIGN KEY (numero_cuenta)
    REFERENCES cuenta(numero_cuenta)
    );
