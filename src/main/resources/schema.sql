CREATE TABLE IF NOT EXISTS atividade_idempotencia (
    estudante_id BIGINT NOT NULL REFERENCES estudantes(id) ON DELETE CASCADE,
    chave VARCHAR(128) NOT NULL,
    payload_hash VARCHAR(64) NOT NULL,
    status_http SMALLINT,
    resposta_json TEXT,
    expira_em TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (estudante_id, chave)
);