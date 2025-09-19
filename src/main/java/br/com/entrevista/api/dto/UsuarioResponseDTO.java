package br.com.entrevista.api.dto;

import java.time.LocalDate;
import java.util.Set;

import br.com.entrevista.api.entities.Roles;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
	private Long id;
    private String primeiroNome;
    private String ultimoNome;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private Set<Roles> roles;
}
