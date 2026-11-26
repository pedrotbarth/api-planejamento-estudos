package br.edu.unisatc.estudos.repository;

import br.edu.unisatc.estudos.entity.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudanteRepository extends JpaRepository<Estudante, Long> {
}
