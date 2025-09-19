package br.com.entrevista.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CriarRegistroEstudoDTO {

	private Long matriculaId;
	private LocalDate data;
	private String categoriaCodigo; // "PESQUISA", "PRATICA", "ASSISTIR_VIDEOAULA"
	private String descricao;
	private LocalDateTime horarioInicio;
	private Integer duracaoMinutos;
}
