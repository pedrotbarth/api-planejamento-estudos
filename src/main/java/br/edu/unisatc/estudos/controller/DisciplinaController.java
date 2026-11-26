package br.edu.unisatc.estudos.controller;

import br.edu.unisatc.estudos.dto.DisciplinaRequest;
import br.edu.unisatc.estudos.dto.DisciplinaResponse;
import br.edu.unisatc.estudos.service.DisciplinaService;
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
@RequestMapping("/disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    public DisciplinaController(DisciplinaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplinaResponse criar(@Valid @RequestBody DisciplinaRequest dados) {
        return service.criar(dados);
    }

    @GetMapping
    public PaginaResponse<DisciplinaResponse> listar(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "nome,asc") String sort
    ) {
        return service.listar(Paginacao.criar(page, size, sort, "nome"));
    }

    @GetMapping("/{id}")
    public DisciplinaResponse buscarPorId(@PathVariable("id") Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public DisciplinaResponse atualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody DisciplinaRequest dados
    ) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}
