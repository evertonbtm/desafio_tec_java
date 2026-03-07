package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class JwtExpiredException extends ApiInternalServerErrorException {

    public JwtExpiredException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}

