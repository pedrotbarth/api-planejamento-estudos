package br.edu.unisatc.estudos.controller;

import br.edu.unisatc.estudos.dto.AlterarAtividadeRequest;
import br.edu.unisatc.estudos.dto.AtividadeRequest;
import br.edu.unisatc.estudos.dto.AtividadeResponse;
import br.edu.unisatc.estudos.dto.RespostaPagina;
import br.edu.unisatc.estudos.entity.StatusAtividade;
import br.edu.unisatc.estudos.error.RequisicaoInvalidaException;
import br.edu.unisatc.estudos.service.AtividadeService;
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
@RequestMapping("/atividades")
public class AtividadeController {
    private static final Set<String> CAMPOS_ORDENACAO = Set.of("id", "dataLimite", "titulo");

    private final AtividadeService service;

    public AtividadeController(AtividadeService service) {
        this.service = service;
    }

    @GetMapping
    public RespostaPagina<AtividadeResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dataLimite,asc") String sort,
            @RequestParam(required = false) StatusAtividade status,
            @RequestParam(required = false) Long disciplinaId
    ) {
        if (disciplinaId != null && disciplinaId < 1) {
            throw new RequisicaoInvalidaException("disciplinaId deve ser positivo.");
        }
        return service.listar(status, disciplinaId, Paginacao.criar(page, size, sort, CAMPOS_ORDENACAO));
    }

    @GetMapping("/{id}")
    public AtividadeResponse buscar(@PathVariable long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<AtividadeResponse> criar(@Valid @RequestBody AtividadeRequest dados) {
        AtividadeResponse resposta = service.criar(dados);
        return ResponseEntity.created(URI.create("/atividades/" + resposta.id())).body(resposta);
    }

    @PutMapping("/{id}")
    public AtividadeResponse atualizar(@PathVariable long id, @Valid @RequestBody AlterarAtividadeRequest dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable long id) {
        service.excluir(id);
    }
}
