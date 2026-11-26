package br.edu.unisatc.estudos.dto;

import br.edu.unisatc.estudos.entity.StatusAtividade;
import br.edu.unisatc.estudos.entity.TipoAtividade;

import java.time.LocalDate;

public record AtividadeResponse(
        Long id,
        String titulo,
        TipoAtividade tipo,
        LocalDate dataLimite,
        StatusAtividade status,
        Long disciplinaId
) {
}
