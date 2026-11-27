package br.edu.unisatc.estudos.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AtividadeIdempotenciaRepository {

    private final JdbcTemplate jdbc;

    public AtividadeIdempotenciaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean reservar(Long estudanteId, String chave, String payloadHash) {
        int linhas = jdbc.update("""
                INSERT INTO atividade_idempotencia
                    (estudante_id, chave, payload_hash, expira_em)
                VALUES (?, ?, ?, clock_timestamp() + INTERVAL '24 hours')
                ON CONFLICT (estudante_id, chave) DO UPDATE
                SET payload_hash = EXCLUDED.payload_hash,
                    status_http = NULL,
                    resposta_json = NULL,
                    expira_em = clock_timestamp() + INTERVAL '24 hours'
                WHERE atividade_idempotencia.expira_em <= clock_timestamp()
                """, estudanteId, chave, payloadHash);

        return linhas == 1;
    }

    public RespostaRegistrada buscar(Long estudanteId, String chave) {
        return jdbc.queryForObject("""
                SELECT payload_hash, status_http, resposta_json
                FROM atividade_idempotencia
                WHERE estudante_id = ? AND chave = ?
                """, (linha, numero) -> new RespostaRegistrada(
                linha.getString("payload_hash"),
                linha.getInt("status_http"),
                linha.getString("resposta_json")
        ), estudanteId, chave);
    }

    public void salvarResposta(Long estudanteId, String chave, int statusHttp, String respostaJson) {
        int linhas = jdbc.update("""
                UPDATE atividade_idempotencia
                SET status_http = ?,
                    resposta_json = ?,
                    expira_em = clock_timestamp() + INTERVAL '24 hours'
                WHERE estudante_id = ? AND chave = ?
                """, statusHttp, respostaJson, estudanteId, chave);

        if (linhas != 1) {
            throw new IllegalStateException("A reserva da chave de idempotência não foi encontrada.");
        }
    }

    public record RespostaRegistrada(String payloadHash, int statusHttp, String respostaJson) {
    }
}