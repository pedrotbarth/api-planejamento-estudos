package br.edu.unisatc.estudos.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record RespostaPagina<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> RespostaPagina<T> de(Page<T> resultado) {
        return new RespostaPagina<>(resultado.getContent(), resultado.getNumber(), resultado.getSize(),
                resultado.getTotalElements(), resultado.getTotalPages());
    }
}
