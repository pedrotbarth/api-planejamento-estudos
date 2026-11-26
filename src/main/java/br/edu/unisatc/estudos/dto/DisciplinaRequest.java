package br.edu.unisatc.estudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DisciplinaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 120, message = "O nome deve ter até 120 caracteres.")
        String nome,

        @NotNull(message = "Informe o ID do estudante.")
        @Positive(message = "O ID do estudante deve ser positivo.")
        Long estudanteId
) {
}