package br.com.entrevista.api.dto;

import lombok.Data;

@Data
public class AtualizarUsuarioDTO {

	private String primeiroNome;
    private String ultimoNome;
    private String telefone;
}
