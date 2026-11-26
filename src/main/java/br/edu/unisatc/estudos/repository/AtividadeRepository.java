package br.edu.unisatc.estudos.repository;

import br.edu.unisatc.estudos.entity.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.unisatc.estudos.entity.StatusAtividade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    @Query("""
       select a from Atividade a
       where (:status is null or a.status = :status)
         and (:disciplinaId is null or a.disciplina.id = :disciplinaId)
       """)
    Page<Atividade> listarFiltradas(
            @Param("status") StatusAtividade status,
            @Param("disciplinaId") Long disciplinaId,
            Pageable pageable
    );
}
