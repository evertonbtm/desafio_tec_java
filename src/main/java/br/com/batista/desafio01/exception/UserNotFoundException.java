package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserNotFoundException extends ApiInternalServerErrorException {

    public UserNotFoundException(Class clazz, String field, String code){
        super(clazz.getSimpleName() + " not found "+ field + " = "+code);
    }

}
