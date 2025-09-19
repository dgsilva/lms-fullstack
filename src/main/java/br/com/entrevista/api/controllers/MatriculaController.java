package br.com.entrevista.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.entrevista.api.dto.MatricularDTO;
import br.com.entrevista.api.entities.Matricula;
import br.com.entrevista.api.servicies.MatriculaService;
import br.com.entrevista.api.util.SecurityUtils;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @PostMapping
    public ResponseEntity<Matricula> matricular(@RequestBody MatricularDTO dto) {
        Long estudanteId = SecurityUtils.getUsuarioId();
        Matricula matricula = matriculaService.matricularEstudante(dto, estudanteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
    }

    @GetMapping("/estudante")
    public ResponseEntity<List<Matricula>> listarMatriculasDoEstudante() {
        Long estudanteId = SecurityUtils.getUsuarioId();
        List<Matricula> matriculas = matriculaService.listarMatriculasDoEstudante(estudanteId);
        return ResponseEntity.ok(matriculas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarMatricula(@PathVariable Long id) {
        Long estudanteId = SecurityUtils.getUsuarioId();
        matriculaService.cancelarMatricula(id, estudanteId);
        return ResponseEntity.noContent().build();
    }
}
