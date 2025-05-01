package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserAlreadyCreatedException extends ApiInternalServerErrorException {

    public UserAlreadyCreatedException(Class clazz, String field, String code){
        super(clazz.getSimpleName() + " already created "+ field + " = "+code);
    }
}
