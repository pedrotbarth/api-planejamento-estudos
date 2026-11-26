package br.edu.unisatc.estudos.dto;

import br.edu.unisatc.estudos.entity.TipoAtividade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AtividadeRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 160, message = "O título deve ter até 160 caracteres.")
        String titulo,

        @NotNull(message = "Informe o tipo da atividade.")
        TipoAtividade tipo,

        @NotNull(message = "Informe a data-limite.")
        LocalDate dataLimite,

        @NotNull(message = "Informe o ID da disciplina.")
        @Positive(message = "O ID da disciplina deve ser positivo.")
        Long disciplinaId
) {
}