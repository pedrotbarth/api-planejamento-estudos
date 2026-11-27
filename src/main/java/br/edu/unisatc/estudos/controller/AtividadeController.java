package br.edu.unisatc.estudos.controller;

import br.edu.unisatc.estudos.dto.AtividadeRequest;
import br.edu.unisatc.estudos.dto.AtividadeResponse;
import br.edu.unisatc.estudos.service.AtividadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.edu.unisatc.estudos.dto.AtividadeUpdateRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import br.edu.unisatc.estudos.dto.PaginaResponse;
import br.edu.unisatc.estudos.entity.StatusAtividade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    private final AtividadeService service;

    public AtividadeController(AtividadeService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtividadeResponse criar(@Valid @RequestBody AtividadeRequest dados) {
        return service.criar(dados);
    }

    @GetMapping
    public PaginaResponse<AtividadeResponse> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "dataLimite,asc") String sort,
            @RequestParam(name = "status", required = false) StatusAtividade status,
            @RequestParam(name = "disciplinaId", required = false) Long disciplinaId
    ) {
        if (disciplinaId != null && disciplinaId < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "disciplinaId deve ser positivo.");
        }

        return service.listar(
                status, disciplinaId, Paginacao.criar(page, size, sort, "dataLimite")
        );
    }

    @GetMapping("/{id}")
    public AtividadeResponse buscarPorId(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AtividadeResponse atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody AtividadeUpdateRequest dados
    ) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}
