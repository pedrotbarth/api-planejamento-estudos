package br.edu.unisatc.estudos.service;

import br.edu.unisatc.estudos.dto.EstudanteRequest;
import br.edu.unisatc.estudos.dto.EstudanteResponse;
import br.edu.unisatc.estudos.entity.Estudante;
import br.edu.unisatc.estudos.repository.EstudanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.edu.unisatc.estudos.dto.PaginaResponse;
import org.springframework.data.domain.Pageable;

@Service
public class EstudanteService {

    private final EstudanteRepository repository;

    public EstudanteService(EstudanteRepository repository) {
        this.repository = repository;
    }

    public EstudanteResponse criar(EstudanteRequest dados) {
        Estudante estudante = new Estudante(dados.nome().strip());
        Estudante salvo = repository.save(estudante);
        return new EstudanteResponse(salvo.getId(), salvo.getNome());
    }

    public PaginaResponse<EstudanteResponse> listar(Pageable pageable) {
        return PaginaResponse.de(
                repository.findAll(pageable)
                        .map(estudante -> new EstudanteResponse(estudante.getId(), estudante.getNome()))
        );
    }

    public EstudanteResponse buscarPorId(Long id) {
        Estudante estudante = encontrar(id);
        return new EstudanteResponse(estudante.getId(), estudante.getNome());
    }

    @Transactional
    public EstudanteResponse atualizar(Long id, EstudanteRequest dados) {
        Estudante estudante = encontrar(id);
        estudante.alterarNome(dados.nome().strip());
        return new EstudanteResponse(estudante.getId(), estudante.getNome());
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(encontrar(id));
    }

    private Estudante encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudante não encontrado"));
    }
}
