package br.com.entrevista.api.servicies;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.entrevista.api.dto.CriarCursoDTO;
import br.com.entrevista.api.entities.Curso;
import br.com.entrevista.api.entities.Usuario;
import br.com.entrevista.api.exception.RegraNegocioException;
import br.com.entrevista.api.repositories.CursoRepository;
import br.com.entrevista.api.repositories.UsuarioRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public CursoService(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Curso criarCurso(CriarCursoDTO dto, Long adminId) {
        validarNomeUnico(dto.getNome());
        validarDuracaoCurso(dto.getDataInicio(), dto.getDataFim());
        
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RegraNegocioException("Administrador não encontrado."));
        
        validarPermissaoAdmin(admin);

        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setDescricao(dto.getDescricao());
        curso.setDataInicio(dto.getDataInicio());
        curso.setDataFim(dto.getDataFim());
        curso.setCriadoPor(admin);

        return cursoRepository.save(curso);
    }

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Curso não encontrado."));
    }

    @Transactional
    public Curso atualizarCurso(Long id, CriarCursoDTO dto, Long adminId) {
        Curso curso = buscarPorId(id);
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RegraNegocioException("Administrador não encontrado."));
        
        validarPermissaoAdmin(admin);

        if (!curso.getNome().equals(dto.getNome())) {
            validarNomeUnico(dto.getNome());
        }
        
        validarDuracaoCurso(dto.getDataInicio(), dto.getDataFim());

        curso.setNome(dto.getNome());
        curso.setDescricao(dto.getDescricao());
        curso.setDataInicio(dto.getDataInicio());
        curso.setDataFim(dto.getDataFim());

        return cursoRepository.save(curso);
    }

    @Transactional
    public void removerCurso(Long id, Long adminId) {
        Curso curso = buscarPorId(id);
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RegraNegocioException("Administrador não encontrado."));
        
        validarPermissaoAdmin(admin);
        
        cursoRepository.delete(curso);
    }

    private void validarNomeUnico(String nome) {
        if (cursoRepository.existsByNomeIgnoreCase(nome)) {
            throw new RegraNegocioException("Nome do curso já existe.");
        }
    }

    private void validarDuracaoCurso(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate limiteMaximo = dataInicio.plusMonths(6);
        if (dataFim.isAfter(limiteMaximo)) {
            throw new RegraNegocioException("O curso deve ser concluído dentro de 6 meses a partir da data de início.");
        }
    }

    private void validarPermissaoAdmin(Usuario usuario) {
        boolean isAdmin = usuario.getRole().stream()
                .anyMatch(role -> "ADMIN".equals(role.getNome()));
        
        if (!isAdmin) {
            throw new RegraNegocioException("Apenas administradores podem gerenciar cursos.");
        }
    }
}
