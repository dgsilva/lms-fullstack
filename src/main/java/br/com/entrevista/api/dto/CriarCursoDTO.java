package br.com.entrevista.api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CriarCursoDTO {

	private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
