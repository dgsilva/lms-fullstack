package br.com.entrevista.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.entrevista.api.entities.Roles;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByNome(String nome); // "ADMIN" ou "STUDENT"
}
