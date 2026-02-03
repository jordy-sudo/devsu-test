ALTER TABLE movimiento
    ADD COLUMN IF NOT EXISTS estado BOOLEAN NOT NULL DEFAULT true;

CREATE INDEX IF NOT EXISTS idx_mov_estado ON movimiento(estado);