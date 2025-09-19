package br.com.entrevista.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.entrevista.api.entities.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    int countByEstudanteIdAndStatus(Long estudanteId, String status);
    boolean existsByEstudanteIdAndCursoId(Long estudanteId, Long cursoId);
    List<Matricula> findByEstudanteId(Long estudanteId);
}
