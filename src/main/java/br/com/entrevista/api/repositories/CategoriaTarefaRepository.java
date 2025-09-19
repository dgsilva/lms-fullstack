package br.com.entrevista.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.entrevista.api.entities.CategoriaTarefa;

public interface CategoriaTarefaRepository extends JpaRepository<CategoriaTarefa, Long> {
    Optional<CategoriaTarefa> findByCodigo(String codigo);
}
