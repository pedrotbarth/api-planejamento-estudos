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

@Service
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final DisciplinaRepository disciplinaRepository;

    public AtividadeService(
            AtividadeRepository atividadeRepository,
            DisciplinaRepository disciplinaRepository
    ) {
        this.atividadeRepository = atividadeRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @Transactional
    public AtividadeResponse criar(AtividadeRequest dados) {
        Disciplina disciplina = encontrarDisciplina(dados.disciplinaId());
        Atividade atividade = new Atividade(
                dados.titulo().strip(), dados.tipo(), dados.dataLimite(), disciplina
        );

        return converter(atividadeRepository.save(atividade));
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
