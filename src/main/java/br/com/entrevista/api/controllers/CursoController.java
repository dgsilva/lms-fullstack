package br.com.entrevista.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.entrevista.api.dto.CriarCursoDTO;
import br.com.entrevista.api.entities.Curso;
import br.com.entrevista.api.servicies.CursoService;
import br.com.entrevista.api.util.SecurityUtils;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Curso> criarCurso(@RequestBody CriarCursoDTO dto) {
        Long adminId = SecurityUtils.getUsuarioId();
        Curso curso = cursoService.criarCurso(dto, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listarCursos() {
        List<Curso> cursos = cursoService.listarCursos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarCurso(@PathVariable Long id) {
        Curso curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> atualizarCurso(@PathVariable Long id, @RequestBody CriarCursoDTO dto) {
        Long adminId = SecurityUtils.getUsuarioId();
        Curso curso = cursoService.atualizarCurso(id, dto, adminId);
        return ResponseEntity.ok(curso);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCurso(@PathVariable Long id) {
        Long adminId = SecurityUtils.getUsuarioId();
        cursoService.removerCurso(id, adminId);
        return ResponseEntity.noContent().build();
    }
}
