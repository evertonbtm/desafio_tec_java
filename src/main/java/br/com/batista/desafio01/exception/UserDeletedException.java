package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserDeletedException extends ApiInternalServerErrorException {

    public UserDeletedException(Class clazz, String field, String code){
        super(clazz.getSimpleName() + " deleted error "+ field + " = "+code);
    }

}
