package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class JwtUnsupportedException extends ApiInternalServerErrorException {

    public JwtUnsupportedException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}

