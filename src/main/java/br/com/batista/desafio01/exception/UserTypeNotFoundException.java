package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UserTypeNotFoundException extends ApiInternalServerErrorException {

    private String code;

    public UserTypeNotFoundException(Class clazz, String field){
        super(clazz.getSimpleName() + " :  UserType not found "+ field );
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
