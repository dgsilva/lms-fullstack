package br.com.entrevista.api.dto;

import java.util.Set;

import br.com.entrevista.api.entities.Roles;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String email;
    private String primeiroNome;
    private String ultimoNome;
    private Set<Roles> roles;
}
