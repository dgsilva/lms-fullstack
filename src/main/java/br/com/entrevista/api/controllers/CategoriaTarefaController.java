package br.com.entrevista.api.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.entrevista.api.entities.CategoriaTarefa;
import br.com.entrevista.api.repositories.CategoriaTarefaRepository;

@RestController
@RequestMapping("/categorias-tarefas")
public class CategoriaTarefaController {

    private final CategoriaTarefaRepository categoriaTarefaRepository;

    public CategoriaTarefaController(CategoriaTarefaRepository categoriaTarefaRepository) {
        this.categoriaTarefaRepository = categoriaTarefaRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaTarefa>> listarCategorias() {
        List<CategoriaTarefa> categorias = categoriaTarefaRepository.findAll();
        return ResponseEntity.ok(categorias);
    }
}
