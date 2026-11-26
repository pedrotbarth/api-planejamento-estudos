package br.edu.unisatc.estudos.service;

import br.edu.unisatc.estudos.dto.EstudanteRequest;
import br.edu.unisatc.estudos.dto.EstudanteResponse;
import br.edu.unisatc.estudos.dto.RespostaPagina;
import br.edu.unisatc.estudos.entity.Estudante;
import br.edu.unisatc.estudos.error.RecursoNaoEncontradoException;
import br.edu.unisatc.estudos.repository.EstudanteRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstudanteService {
    private final EstudanteRepository repository;

    public EstudanteService(EstudanteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public RespostaPagina<EstudanteResponse> listar(Pageable pageable) {
        return RespostaPagina.de(repository.findAll(pageable).map(this::resposta));
    }

    @Transactional(readOnly = true)
    public EstudanteResponse buscar(long id) {
        return resposta(encontrar(id));
    }

    public EstudanteResponse criar(EstudanteRequest dados) {
        return resposta(repository.save(new Estudante(dados.nome().strip())));
    }

    public EstudanteResponse atualizar(long id, EstudanteRequest dados) {
        Estudante estudante = encontrar(id);
        estudante.atualizar(dados.nome().strip());
        return resposta(estudante);
    }

    public void excluir(long id) {
        repository.delete(encontrar(id));
    }

    private Estudante encontrar(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estudante " + id + " não encontrado."));
    }

    private EstudanteResponse resposta(Estudante estudante) {
        return new EstudanteResponse(estudante.getId(), estudante.getNome());
    }
}
