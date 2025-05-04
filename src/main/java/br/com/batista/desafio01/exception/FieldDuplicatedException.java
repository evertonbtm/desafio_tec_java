package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class FieldDuplicatedException extends ApiInternalServerErrorException {

    public FieldDuplicatedException (Class clazz, String field, String code){
        super(clazz.getSimpleName() + " : " + field + " is duplicated. code = "+code);
    }
}
