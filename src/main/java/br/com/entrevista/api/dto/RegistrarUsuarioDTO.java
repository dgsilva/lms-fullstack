package br.com.entrevista.api.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class RegistrarUsuarioDTO {

	private String primeiroNome;
    private String ultimoNome;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private String senha;
}
