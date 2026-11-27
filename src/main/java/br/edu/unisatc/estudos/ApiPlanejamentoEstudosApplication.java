package br.edu.unisatc.estudos;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "API de Planejamento de Estudos",
		version = "1.0",
		description = "Cadastro de estudantes, disciplinas e atividades."
))
@SpringBootApplication
public class ApiPlanejamentoEstudosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiPlanejamentoEstudosApplication.class, args);
	}

}
