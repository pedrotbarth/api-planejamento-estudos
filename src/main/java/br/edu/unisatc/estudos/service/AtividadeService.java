package br.edu.unisatc.estudos.service;

import br.edu.unisatc.estudos.dto.AlterarAtividadeRequest;
import br.edu.unisatc.estudos.dto.AtividadeRequest;
import br.edu.unisatc.estudos.dto.AtividadeResponse;
import br.edu.unisatc.estudos.dto.RespostaPagina;
import br.edu.unisatc.estudos.entity.Atividade;
import br.edu.unisatc.estudos.entity.Disciplina;
import br.edu.unisatc.estudos.entity.StatusAtividade;
import br.edu.unisatc.estudos.error.RecursoNaoEncontradoException;
import br.edu.unisatc.estudos.repository.AtividadeRepository;
import br.edu.unisatc.estudos.repository.DisciplinaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AtividadeService {
    private final AtividadeRepository repository;
    private final DisciplinaRepository disciplinaRepository;

    public AtividadeService(AtividadeRepository repository, DisciplinaRepository disciplinaRepository) {
        this.repository = repository;
        this.disciplinaRepository = disciplinaRepository;
    }

    @Transactional(readOnly = true)
    public RespostaPagina<AtividadeResponse> listar(StatusAtividade status, Long disciplinaId, Pageable pageable) {
        return RespostaPagina.de(repository.listar(status, disciplinaId, pageable).map(this::resposta));
    }

    @Transactional(readOnly = true)
    public AtividadeResponse buscar(long id) {
        return resposta(encontrar(id));
    }

    public AtividadeResponse criar(AtividadeRequest dados) {
        Disciplina disciplina = disciplinaRepository.findById(dados.disciplinaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina " + dados.disciplinaId() + " não encontradoa."));
        return resposta(repository.save(new Atividade(dados.titulo().strip(), dados.tipo(), dados.dataLimite(), disciplina)));
    }

    public AtividadeResponse atualizar(long id, AlterarAtividadeRequest dados) {
        Atividade atividade = encontrar(id);
        Disciplina disciplina = disciplinaRepository.findById(dados.disciplinaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina " + dados.disciplinaId() + " não encontradoa."));
        atividade.atualizar(dados.titulo().strip(), dados.tipo(), dados.dataLimite(), dados.status(), disciplina);
        return resposta(atividade);
    }

    public void excluir(long id) {
        repository.delete(encontrar(id));
    }

    private Atividade encontrar(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade " + id + " não encontradoa."));
    }

    private AtividadeResponse resposta(Atividade atividade) {
        return new AtividadeResponse(atividade.getId(), atividade.getTitulo(), atividade.getTipo(),
                atividade.getDataLimite(), atividade.getStatus(), atividade.getDisciplina().getId());
    }
}
