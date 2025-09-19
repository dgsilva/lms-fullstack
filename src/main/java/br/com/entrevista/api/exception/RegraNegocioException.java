package br.com.entrevista.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class RegraNegocioException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public RegraNegocioException(String message) {
		super(message);
	}

}
