package br.com.entrevista.api.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String senha;
}
