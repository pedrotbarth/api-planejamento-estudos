package br.edu.unisatc.estudos.controller;

import br.edu.unisatc.estudos.dto.EstudanteRequest;
import br.edu.unisatc.estudos.dto.EstudanteResponse;
import br.edu.unisatc.estudos.dto.RespostaPagina;
import br.edu.unisatc.estudos.service.EstudanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {
    private static final Set<String> CAMPOS_ORDENACAO = Set.of("id", "nome");

    private final EstudanteService service;

    public EstudanteController(EstudanteService service) {
        this.service = service;
    }

    @GetMapping
    public RespostaPagina<EstudanteResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        return service.listar(Paginacao.criar(page, size, sort, CAMPOS_ORDENACAO));
    }

    @GetMapping("/{id}")
    public EstudanteResponse buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<EstudanteResponse> criar(@Valid @RequestBody EstudanteRequest dados) {
        EstudanteResponse resposta = service.criar(dados);
        return ResponseEntity.created(URI.create("/estudantes/" + resposta.id())).body(resposta);
    }

    @PutMapping("/{id}")
    public EstudanteResponse atualizar(@PathVariable long id, @Valid @RequestBody EstudanteRequest dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        service.excluir(id);
    }
}
