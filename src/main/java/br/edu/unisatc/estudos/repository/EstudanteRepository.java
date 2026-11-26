package br.edu.unisatc.estudos.repository;

import br.edu.unisatc.estudos.entity.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.unisatc.estudos.dto.PaginaResponse;
import org.springframework.data.domain.Pageable;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
}