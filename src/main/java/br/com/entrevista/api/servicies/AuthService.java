package br.com.entrevista.api.servicies;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.entrevista.api.config.JwtUtil;
import br.com.entrevista.api.dto.AuthResponseDTO;
import br.com.entrevista.api.dto.LoginDTO;
import br.com.entrevista.api.entities.Usuario;
import br.com.entrevista.api.servicies.CustomUserDetailsService.CustomUserPrincipal;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        CustomUserPrincipal customUserPrincipal = (CustomUserPrincipal) userDetails;
        Usuario usuario = customUserPrincipal.getUsuario();

        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setEmail(usuario.getEmail());
        response.setPrimeiroNome(usuario.getPrimeiroNome());
        response.setUltimoNome(usuario.getUltimoNome());
        response.setRoles(usuario.getRole());

        return response;
    }
}
