package br.edu.unisatc.estudos.controller;

import br.edu.unisatc.estudos.error.RequisicaoInvalidaException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

final class Paginacao {
    private Paginacao() {
    }

    static PageRequest criar(int page, int size, String sort, Set<String> camposPermitidos) {
        if (page < 0 || size < 1 || size > 100 || (long) page * size > Integer.MAX_VALUE) {
            throw new RequisicaoInvalidaException("Use page >= 0, size entre 1 e 100 e um deslocamento válido.");
        }

        String[] partes = sort.split(",", -1);
        if (partes.length != 2 || !camposPermitidos.contains(partes[0])
                || (!"asc".equalsIgnoreCase(partes[1]) && !"desc".equalsIgnoreCase(partes[1]))) {
            throw new RequisicaoInvalidaException("Use sort=campo,asc ou sort=campo,desc. Campos: "
                    + camposPermitidos.stream().sorted().toList() + ".");
        }

        Sort ordem = Sort.by(Sort.Direction.fromString(partes[1]), partes[0]);
        if (!"id".equals(partes[0])) {
            ordem = ordem.and(Sort.by("id"));
        }
        return PageRequest.of(page, size, ordem);
    }
}
