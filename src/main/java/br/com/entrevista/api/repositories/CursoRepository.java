package br.com.entrevista.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.entrevista.api.entities.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
