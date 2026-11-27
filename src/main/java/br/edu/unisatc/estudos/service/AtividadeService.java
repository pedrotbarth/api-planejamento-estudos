package br.edu.unisatc.estudos.service;

import br.edu.unisatc.estudos.dto.AtividadeRequest;
import br.edu.unisatc.estudos.dto.AtividadeResponse;
import br.edu.unisatc.estudos.entity.Atividade;
import br.edu.unisatc.estudos.entity.Disciplina;
import br.edu.unisatc.estudos.repository.AtividadeRepository;
import br.edu.unisatc.estudos.repository.DisciplinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.edu.unisatc.estudos.dto.AtividadeUpdateRequest;
import br.edu.unisatc.estudos.dto.PaginaResponse;
import br.edu.unisatc.estudos.entity.StatusAtividade;
import org.springframework.data.domain.Pageable;
import br.edu.unisatc.estudos.repository.AtividadeIdempotenciaRepository;
import tools.jackson.databind.json.JsonMapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final DisciplinaRepository disciplinaRepository;

    private final AtividadeIdempotenciaRepository idempotenciaRepository;
    private final JsonMapper jsonMapper;

    public AtividadeService(
            AtividadeRepository atividadeRepository,
            DisciplinaRepository disciplinaRepository,
            AtividadeIdempotenciaRepository idempotenciaRepository,
            JsonMapper jsonMapper
    ) {
        this.atividadeRepository = atividadeRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.idempotenciaRepository = idempotenciaRepository;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public AtividadeResponse criar(AtividadeRequest dados) {
        return cadastrar(dados, encontrarDisciplina(dados.disciplinaId()));
    }

    @Transactional
    public ResultadoCriacao criarComIdempotencia(AtividadeRequest dados, String chave) {
        if (chave.isBlank() || chave.length() > 128) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Idempotency-Key deve conter de 1 a 128 caracteres.");
        }

        Disciplina disciplina = encontrarDisciplina(dados.disciplinaId());
        Long estudanteId = disciplina.getEstudante().getId();
        String payloadHash = calcularHash(dados);

        if (!idempotenciaRepository.reservar(estudanteId, chave, payloadHash)) {
            var resposta = idempotenciaRepository.buscar(estudanteId, chave);

            if (!resposta.payloadHash().equals(payloadHash)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "A chave já foi usada com dados diferentes.");
            }

            return new ResultadoCriacao(resposta.statusHttp(), resposta.respostaJson());
        }

        AtividadeResponse atividade = cadastrar(dados, disciplina);
        String respostaJson = jsonMapper.writeValueAsString(atividade);
        int statusHttp = HttpStatus.CREATED.value();
        idempotenciaRepository.salvarResposta(estudanteId, chave, statusHttp, respostaJson);

        return new ResultadoCriacao(statusHttp, respostaJson);
    }

    private AtividadeResponse cadastrar(AtividadeRequest dados, Disciplina disciplina) {
        Atividade atividade = new Atividade(
                dados.titulo().strip(), dados.tipo(), dados.dataLimite(), disciplina
        );
        return converter(atividadeRepository.save(atividade));
    }

    private String calcularHash(AtividadeRequest dados) {
        try {
            byte[] json = jsonMapper.writeValueAsBytes(dados);
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(json);
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException erro) {
            throw new IllegalStateException("SHA-256 indisponível.", erro);
        }
    }

    public record ResultadoCriacao(int statusHttp, String respostaJson) {
    }

    @Transactional(readOnly = true)
    public PaginaResponse<AtividadeResponse> listar(
            StatusAtividade status,
            Long disciplinaId,
            Pageable pageable
    ) {
        return PaginaResponse.de(
                atividadeRepository.listarFiltradas(status, disciplinaId, pageable)
                        .map(this::converter)
        );
    }

    @Transactional(readOnly = true)
    public AtividadeResponse buscarPorId(Long id) {
        return converter(encontrarAtividade(id));
    }

    @Transactional
    public AtividadeResponse atualizar(Long id, AtividadeUpdateRequest dados) {
        Atividade atividade = encontrarAtividade(id);
        Disciplina disciplina = encontrarDisciplina(dados.disciplinaId());

        atividade.atualizar(
                dados.titulo().strip(),
                dados.tipo(),
                dados.dataLimite(),
                dados.status(),
                disciplina
        );

        return converter(atividade);
    }

    @Transactional
    public void excluir(Long id) {
        atividadeRepository.delete(encontrarAtividade(id));
    }

    private Atividade encontrarAtividade(Long id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Atividade não encontrada"));
    }

    private Disciplina encontrarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disciplina não encontrada"));
    }

    private AtividadeResponse converter(Atividade atividade) {
        return new AtividadeResponse(
                atividade.getId(),
                atividade.getTitulo(),
                atividade.getTipo(),
                atividade.getDataLimite(),
                atividade.getStatus(),
                atividade.getDisciplina().getId()
        );
    }
}
