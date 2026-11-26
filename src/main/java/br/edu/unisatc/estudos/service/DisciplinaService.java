package br.edu.unisatc.estudos.service;

import br.edu.unisatc.estudos.dto.DisciplinaRequest;
import br.edu.unisatc.estudos.dto.DisciplinaResponse;
import br.edu.unisatc.estudos.dto.RespostaPagina;
import br.edu.unisatc.estudos.entity.Disciplina;
import br.edu.unisatc.estudos.entity.Estudante;
import br.edu.unisatc.estudos.error.RecursoNaoEncontradoException;
import br.edu.unisatc.estudos.repository.DisciplinaRepository;
import br.edu.unisatc.estudos.repository.EstudanteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisciplinaService {
    private final DisciplinaRepository repository;
    private final EstudanteRepository estudanteRepository;

    public DisciplinaService(DisciplinaRepository repository, EstudanteRepository estudanteRepository) {
        this.repository = repository;
        this.estudanteRepository = estudanteRepository;
    }

    @Transactional(readOnly = true)
    public RespostaPagina<DisciplinaResponse> listar(Pageable pageable) {
        return RespostaPagina.de(repository.findAll(pageable).map(this::resposta));
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscar(long id) {
        return resposta(encontrar(id));
    }

    public DisciplinaResponse criar(DisciplinaRequest dados) {
        Estudante estudante = estudanteRepository.findById(dados.estudanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estudante " + dados.estudanteId() + " não encontrado."));
        return resposta(repository.save(new Disciplina(dados.nome().strip(), estudante)));
    }

    public DisciplinaResponse atualizar(long id, DisciplinaRequest dados) {
        Disciplina disciplina = encontrar(id);
        Estudante estudante = estudanteRepository.findById(dados.estudanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estudante " + dados.estudanteId() + " não encontrado."));
        disciplina.atualizar(dados.nome().strip(), estudante);
        return resposta(disciplina);
    }

    public void excluir(long id) {
        repository.delete(encontrar(id));
    }

    private Disciplina encontrar(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Disciplina " + id + " não encontradoa."));
    }

    private DisciplinaResponse resposta(Disciplina disciplina) {
        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(), disciplina.getEstudante().getId());
    }
}
