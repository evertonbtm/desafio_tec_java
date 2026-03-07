package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class JwtInvalidException extends ApiInternalServerErrorException {

    public JwtInvalidException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    public JwtInvalidException(String message) {
        super(message);
    }
}

