package br.com.entrevista.api.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.entrevista.api.entities.RegistroEstudo;

public interface RegistroEstudoRepository extends JpaRepository<RegistroEstudo, Long> {
    List<RegistroEstudo> findByMatriculaIdAndDataBetween(Long matriculaId, LocalDate inicio, LocalDate fim);
    List<RegistroEstudo> findByMatriculaEstudanteId(Long estudanteId);
    List<RegistroEstudo> findByMatriculaId(Long matriculaId);
}
