package br.edu.unisatc.estudos.controller;

import br.edu.unisatc.estudos.dto.EstudanteRequest;
import br.edu.unisatc.estudos.dto.EstudanteResponse;
import br.edu.unisatc.estudos.service.EstudanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import br.edu.unisatc.estudos.dto.PaginaResponse;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

    private final EstudanteService service;

    public EstudanteController(EstudanteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstudanteResponse criar(@Valid @RequestBody EstudanteRequest dados) {
        return service.criar(dados);
    }

    @GetMapping
    public PaginaResponse<EstudanteResponse> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "nome,asc") String sort
    ) {
        return service.listar(Paginacao.criar(page, size, sort, "nome"));
    }

    @GetMapping("/{id}")
    public EstudanteResponse buscarPorId(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public EstudanteResponse atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody EstudanteRequest dados
    ) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}
