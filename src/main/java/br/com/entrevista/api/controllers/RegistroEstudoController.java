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

import br.com.entrevista.api.dto.CriarRegistroEstudoDTO;
import br.com.entrevista.api.entities.RegistroEstudo;
import br.com.entrevista.api.servicies.RegistroEstudoService;
import br.com.entrevista.api.util.SecurityUtils;

@RestController
@RequestMapping("/registros-estudo")
public class RegistroEstudoController {

    private final RegistroEstudoService registroEstudoService;

    public RegistroEstudoController(RegistroEstudoService registroEstudoService) {
        this.registroEstudoService = registroEstudoService;
    }

    @PostMapping
    public ResponseEntity<RegistroEstudo> criarRegistro(@RequestBody CriarRegistroEstudoDTO dto) {
        Long estudanteId = SecurityUtils.getUsuarioId();
        RegistroEstudo registro = registroEstudoService.criarRegistro(dto, estudanteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(registro);
    }

    @GetMapping
    public ResponseEntity<List<RegistroEstudo>> listarRegistrosDoEstudante() {
        Long estudanteId = SecurityUtils.getUsuarioId();
        List<RegistroEstudo> registros = registroEstudoService.listarRegistrosDoEstudante(estudanteId);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/matricula/{matriculaId}")
    public ResponseEntity<List<RegistroEstudo>> listarRegistrosPorMatricula(@PathVariable Long matriculaId) {
        Long estudanteId = SecurityUtils.getUsuarioId();
        List<RegistroEstudo> registros = registroEstudoService.listarRegistrosPorMatricula(matriculaId, estudanteId);
        return ResponseEntity.ok(registros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegistroEstudo> atualizarRegistro(@PathVariable Long id, @RequestBody CriarRegistroEstudoDTO dto) {
        Long estudanteId = SecurityUtils.getUsuarioId();
        RegistroEstudo registro = registroEstudoService.atualizarRegistro(id, dto, estudanteId);
        return ResponseEntity.ok(registro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerRegistro(@PathVariable Long id) {
        Long estudanteId = SecurityUtils.getUsuarioId();
        registroEstudoService.removerRegistro(id, estudanteId);
        return ResponseEntity.noContent().build();
    }
}
