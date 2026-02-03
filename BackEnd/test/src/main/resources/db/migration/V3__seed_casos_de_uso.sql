-- Inserta datos de ejemplo (Casos de Uso)
-- Nota: contraseñas en texto plano SOLO para demo.

-- 1) PERSONA + CLIENTE (mismo id)
WITH p AS (
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono)
VALUES
    ('Jose Lema', 'Masculino', 35, '1710000001', 'Otavalo sn y principal', '0982547985'),
    ('Mariela Montalvo', 'Femenino', 30, '1710000002', 'Amazonas y NNUU',       '097548965'),
    ('Juan Osorio', 'Masculino', 40, '1710000003', '13 junio y Equinoccial',     '098874587')
    RETURNING id, nombre
    )
INSERT INTO cliente (cliente_id, contrasena, estado)
SELECT
    id,
    CASE nombre
        WHEN 'Jose Lema' THEN '1234'
        WHEN 'Mariela Montalvo' THEN '5678'
        WHEN 'Juan Osorio' THEN '1245'
        END,
    TRUE
FROM p;

-- 2) CUENTAS (incluye la nueva cuenta corriente de Jose Lema)
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '478758', 'Ahorro',   2000.00, TRUE, c.cliente_id FROM cliente c
                                                                  JOIN persona p ON p.id = c.cliente_id WHERE p.nombre = 'Jose Lema'
UNION ALL
SELECT '225487', 'Corriente', 100.00, TRUE, c.cliente_id FROM cliente c
                                                                  JOIN persona p ON p.id = c.cliente_id WHERE p.nombre = 'Mariela Montalvo'
UNION ALL
SELECT '495878', 'Ahorros',     0.00, TRUE, c.cliente_id FROM cliente c
                                                                  JOIN persona p ON p.id = c.cliente_id WHERE p.nombre = 'Juan Osorio'
UNION ALL
SELECT '496825', 'Ahorros',   540.00, TRUE, c.cliente_id FROM cliente c
                                                                  JOIN persona p ON p.id = c.cliente_id WHERE p.nombre = 'Mariela Montalvo'
UNION ALL
SELECT '585545', 'Corriente', 1000.00, TRUE, c.cliente_id FROM cliente c
                                                                   JOIN persona p ON p.id = c.cliente_id WHERE p.nombre = 'Jose Lema';

-- 3) MOVIMIENTOS (valor negativo para Retiro, positivo para Deposito)
-- Saldos calculados:
-- 478758: 2000 - 575 = 1425
-- 225487:  100 + 600 = 700
-- 495878:    0 + 150 = 150
-- 496825:  540 - 540 = 0

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, numero_cuenta)
VALUES
    ('2022-02-10', 'Retiro',   -575.00, 1425.00, '478758'),
    ('2022-02-10', 'Deposito',  600.00,  700.00, '225487'),
    ('2022-02-10', 'Deposito',  150.00,  150.00, '495878'),
    ('2022-02-08', 'Retiro',   -540.00,    0.00, '496825');
