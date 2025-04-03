package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserDeletedException extends ApiInternalServerErrorException {

    private String code;

    public UserDeletedException(Class clazz, String field, String code){
        super(clazz.getSimpleName() + " deleted error "+ field + " = "+code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
