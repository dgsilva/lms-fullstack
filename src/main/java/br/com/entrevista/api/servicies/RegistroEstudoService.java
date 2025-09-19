package br.com.entrevista.api.servicies;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.entrevista.api.dto.CriarRegistroEstudoDTO;
import br.com.entrevista.api.entities.CategoriaTarefa;
import br.com.entrevista.api.entities.Matricula;
import br.com.entrevista.api.entities.RegistroEstudo;
import br.com.entrevista.api.exception.RegraNegocioException;
import br.com.entrevista.api.repositories.CategoriaTarefaRepository;
import br.com.entrevista.api.repositories.MatriculaRepository;
import br.com.entrevista.api.repositories.RegistroEstudoRepository;

@Service
public class RegistroEstudoService {

    private final RegistroEstudoRepository registroEstudoRepository;
    private final MatriculaRepository matriculaRepository;
    private final CategoriaTarefaRepository categoriaTarefaRepository;

    public RegistroEstudoService(RegistroEstudoRepository registroEstudoRepository,
                                MatriculaRepository matriculaRepository,
                                CategoriaTarefaRepository categoriaTarefaRepository) {
        this.registroEstudoRepository = registroEstudoRepository;
        this.matriculaRepository = matriculaRepository;
        this.categoriaTarefaRepository = categoriaTarefaRepository;
    }

    @Transactional
    public RegistroEstudo criarRegistro(CriarRegistroEstudoDTO dto, Long estudanteId) {
        Matricula matricula = matriculaRepository.findById(dto.getMatriculaId())
                .orElseThrow(() -> new RegraNegocioException("Matrícula não encontrada."));

        validarMatriculaDoEstudante(matricula, estudanteId);

        CategoriaTarefa categoria = categoriaTarefaRepository.findByCodigo(dto.getCategoriaCodigo())
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada."));

        validarDuracaoIncrementos(dto.getDuracaoMinutos());

        RegistroEstudo registro = new RegistroEstudo();
        registro.setMatricula(matricula);
        registro.setData(dto.getData());
        registro.setCategoria(categoria);
        registro.setDescricao(dto.getDescricao());
        registro.setHorarioInicio(dto.getHorarioInicio());
        registro.setDuracaoMinutos(dto.getDuracaoMinutos());

        return registroEstudoRepository.save(registro);
    }

    public List<RegistroEstudo> listarRegistrosDoEstudante(Long estudanteId) {
        return registroEstudoRepository.findByMatriculaEstudanteId(estudanteId);
    }

    public List<RegistroEstudo> listarRegistrosPorMatricula(Long matriculaId, Long estudanteId) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RegraNegocioException("Matrícula não encontrada."));

        validarMatriculaDoEstudante(matricula, estudanteId);

        return registroEstudoRepository.findByMatriculaId(matriculaId);
    }

    @Transactional
    public RegistroEstudo atualizarRegistro(Long id, CriarRegistroEstudoDTO dto, Long estudanteId) {
        RegistroEstudo registro = registroEstudoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Registro não encontrado."));

        validarMatriculaDoEstudante(registro.getMatricula(), estudanteId);

        CategoriaTarefa categoria = categoriaTarefaRepository.findByCodigo(dto.getCategoriaCodigo())
                .orElseThrow(() -> new RegraNegocioException("Categoria não encontrada."));

        validarDuracaoIncrementos(dto.getDuracaoMinutos());

        registro.setData(dto.getData());
        registro.setCategoria(categoria);
        registro.setDescricao(dto.getDescricao());
        registro.setHorarioInicio(dto.getHorarioInicio());
        registro.setDuracaoMinutos(dto.getDuracaoMinutos());
        registro.setAtualizadoEm(LocalDateTime.now());

        return registroEstudoRepository.save(registro);
    }

    @Transactional
    public void removerRegistro(Long id, Long estudanteId) {
        RegistroEstudo registro = registroEstudoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Registro não encontrado."));

        validarMatriculaDoEstudante(registro.getMatricula(), estudanteId);

        registroEstudoRepository.delete(registro);
    }

    private void validarMatriculaDoEstudante(Matricula matricula, Long estudanteId) {
        if (!matricula.getEstudante().getId().equals(estudanteId)) {
            throw new RegraNegocioException("Você só pode gerenciar registros de suas próprias matrículas.");
        }
    }

    private void validarDuracaoIncrementos(Integer duracaoMinutos) {
        if (duracaoMinutos % 30 != 0) {
            throw new RegraNegocioException("A duração deve ser em incrementos de 30 minutos.");
        }
    }
}
