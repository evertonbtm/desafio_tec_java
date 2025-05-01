package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UnavailableException extends ApiInternalServerErrorException {

    public UnavailableException(Class clazz, String msg){
        super(clazz.getSimpleName() + " : "+ msg);
    }

}
