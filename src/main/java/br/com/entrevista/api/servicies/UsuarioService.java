package br.com.entrevista.api.servicies;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.entrevista.api.dto.RegistrarUsuarioDTO;
import br.com.entrevista.api.entities.Roles;
import br.com.entrevista.api.entities.Usuario;
import br.com.entrevista.api.exception.RegraNegocioException;
import br.com.entrevista.api.repositories.RolesRepository;
import br.com.entrevista.api.repositories.UsuarioRepository;

@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepo;
    private final RolesRepository rolesRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo, RolesRepository rolesRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.rolesRepo = rolesRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarEstudante(RegistrarUsuarioDTO dto) {
        validarIdadeMinima(dto.getDataNascimento());
        if (usuarioRepo.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado.");
        }

        Usuario u = new Usuario();
        u.setPrimeiroNome(dto.getPrimeiroNome());
        u.setUltimoNome(dto.getUltimoNome());
        u.setDataNascimento(dto.getDataNascimento());
        u.setEmail(dto.getEmail());
        u.setTelefone(dto.getTelefone());
        u.setSenha(passwordEncoder.encode(dto.getSenha()));

        Roles estudante = rolesRepo.findByNome("ESTUDANTE")
                .orElseThrow(() -> new RegraNegocioException("Role estudante não configurado."));
        u.setRole(Set.of(estudante));

        return usuarioRepo.save(u);
    }

    @Transactional
    public Usuario criarAdmin(RegistrarUsuarioDTO dto) {
        validarIdadeMinima(dto.getDataNascimento());
        if (usuarioRepo.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado.");
        }

        Usuario u = new Usuario();
        u.setPrimeiroNome(dto.getPrimeiroNome());
        u.setUltimoNome(dto.getUltimoNome());
        u.setDataNascimento(dto.getDataNascimento());
        u.setEmail(dto.getEmail());
        u.setTelefone(dto.getTelefone());
        u.setSenha(passwordEncoder.encode(dto.getSenha()));

        Roles admin = rolesRepo.findByNome("ADMIN")
                .orElseThrow(() -> new RegraNegocioException("Role admin não configurado."));
        u.setRole(Set.of(admin));

        return usuarioRepo.save(u);
    }

    private void validarIdadeMinima(LocalDate nascimento) {
        LocalDate limite = LocalDate.now().minusYears(16);
        if (nascimento.isAfter(limite)) {
            throw new RegraNegocioException("Usuário deve ter pelo menos 16 anos.");
        }
    }
}
