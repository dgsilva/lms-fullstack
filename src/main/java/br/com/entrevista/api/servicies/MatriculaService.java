package br.com.entrevista.api.servicies;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.entrevista.api.dto.MatricularDTO;
import br.com.entrevista.api.entities.Curso;
import br.com.entrevista.api.entities.Matricula;
import br.com.entrevista.api.entities.Usuario;
import br.com.entrevista.api.exception.RegraNegocioException;
import br.com.entrevista.api.repositories.CursoRepository;
import br.com.entrevista.api.repositories.MatriculaRepository;
import br.com.entrevista.api.repositories.UsuarioRepository;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public MatriculaService(MatriculaRepository matriculaRepository, 
                           UsuarioRepository usuarioRepository, 
                           CursoRepository cursoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public Matricula matricularEstudante(MatricularDTO dto, Long estudanteId) {
        Usuario estudante = usuarioRepository.findById(estudanteId)
                .orElseThrow(() -> new RegraNegocioException("Estudante não encontrado."));
        
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado."));

        validarLimiteMatriculas(estudanteId);
        validarMatriculaDuplicada(estudanteId, dto.getCursoId());

        Matricula matricula = new Matricula();
        matricula.setEstudante(estudante);
        matricula.setCurso(curso);
        matricula.setStatus("ATIVO");

        return matriculaRepository.save(matricula);
    }

    public List<Matricula> listarMatriculasDoEstudante(Long estudanteId) {
        return matriculaRepository.findByEstudanteId(estudanteId);
    }

    @Transactional
    public void cancelarMatricula(Long matriculaId, Long estudanteId) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RegraNegocioException("Matrícula não encontrada."));

        if (!matricula.getEstudante().getId().equals(estudanteId)) {
            throw new RegraNegocioException("Você só pode cancelar suas próprias matrículas.");
        }

        matricula.setStatus("CANCELADO");
        matriculaRepository.save(matricula);
    }

    private void validarLimiteMatriculas(Long estudanteId) {
        int matriculasAtivas = matriculaRepository.countByEstudanteIdAndStatus(estudanteId, "ATIVO");
        if (matriculasAtivas >= 3) {
            throw new RegraNegocioException("Estudantes não podem se matricular em mais de 3 cursos ao mesmo tempo.");
        }
    }

    private void validarMatriculaDuplicada(Long estudanteId, Long cursoId) {
        if (matriculaRepository.existsByEstudanteIdAndCursoId(estudanteId, cursoId)) {
            throw new RegraNegocioException("Estudante já está matriculado neste curso.");
        }
    }
}
