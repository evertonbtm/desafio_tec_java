package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserDeleteException extends ApiInternalServerErrorException {

    private String code;

    public UserDeleteException(Class clazz, String field, String code){
        super(clazz.getSimpleName() + " :  User delete error "+ field + " = "+code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
