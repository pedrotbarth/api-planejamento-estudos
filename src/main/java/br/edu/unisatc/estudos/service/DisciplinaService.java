package br.edu.unisatc.estudos.service;

import br.edu.unisatc.estudos.dto.DisciplinaRequest;
import br.edu.unisatc.estudos.dto.DisciplinaResponse;
import br.edu.unisatc.estudos.entity.Disciplina;
import br.edu.unisatc.estudos.entity.Estudante;
import br.edu.unisatc.estudos.repository.DisciplinaRepository;
import br.edu.unisatc.estudos.repository.EstudanteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.edu.unisatc.estudos.dto.PaginaResponse;
import org.springframework.data.domain.Pageable;

@Service
public class DisciplinaService {

    private final DisciplinaRepository disciplinaRepository;
    private final EstudanteRepository estudanteRepository;

    public DisciplinaService(
            DisciplinaRepository disciplinaRepository,
            EstudanteRepository estudanteRepository
    ) {
        this.disciplinaRepository = disciplinaRepository;
        this.estudanteRepository = estudanteRepository;
    }

    @Transactional
    public DisciplinaResponse criar(DisciplinaRequest dados) {
        Estudante estudante = encontrarEstudante(dados.estudanteId());
        Disciplina disciplina = new Disciplina(dados.nome().strip(), estudante);
        return converter(disciplinaRepository.save(disciplina));
    }

    @Transactional(readOnly = true)
    public PaginaResponse<DisciplinaResponse> listar(Pageable pageable) {
        return PaginaResponse.de(
                disciplinaRepository.findAll(pageable).map(this::converter)
        );
    }

    @Transactional(readOnly = true)
    public DisciplinaResponse buscarPorId(Long id) {
        return converter(encontrarDisciplina(id));
    }

    @Transactional
    public DisciplinaResponse atualizar(Long id, DisciplinaRequest dados) {
        Disciplina disciplina = encontrarDisciplina(id);
        Estudante estudante = encontrarEstudante(dados.estudanteId());
        disciplina.atualizar(dados.nome().strip(), estudante);
        return converter(disciplina);
    }

    @Transactional
    public void excluir(Long id) {
        disciplinaRepository.delete(encontrarDisciplina(id));
    }

    private Disciplina encontrarDisciplina(Long id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Disciplina não encontrada"));
    }

    private Estudante encontrarEstudante(Long id) {
        return estudanteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudante não encontrado"));
    }

    private DisciplinaResponse converter(Disciplina disciplina) {
        return new DisciplinaResponse(
                disciplina.getId(),
                disciplina.getNome(),
                disciplina.getEstudante().getId()
        );
    }
}
