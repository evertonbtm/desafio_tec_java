package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserTypeNotFoundException extends ApiInternalServerErrorException {

    public UserTypeNotFoundException(Class clazz, String field){
        super(clazz.getSimpleName() + " not found "+ field );
    }

}
