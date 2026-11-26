package br.edu.unisatc.estudos.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

final class Paginacao {

    private Paginacao() {
    }

    static PageRequest criar(int page, int size, String sort, String campoPermitido) {
        if (page < 0 || size < 1 || size > 100 || (long) page * size > Integer.MAX_VALUE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Informe uma página válida e size entre 1 e 100.");
        }

        String[] ordem = sort.split(",", -1);
        if (ordem.length != 2
                || !campoPermitido.equals(ordem[0])
                || (!"asc".equalsIgnoreCase(ordem[1]) && !"desc".equalsIgnoreCase(ordem[1]))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Use sort=" + campoPermitido + ",asc ou sort=" + campoPermitido + ",desc.");
        }

        Sort ordenacao = Sort.by(Sort.Direction.fromString(ordem[1]), campoPermitido)
                .and(Sort.by("id"));

        return PageRequest.of(page, size, ordenacao);
    }
}