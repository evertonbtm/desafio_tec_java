package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class UnavailableException extends ApiInternalServerErrorException {

    private String code;

    public UnavailableException(Class clazz, String msg){
        super(clazz.getSimpleName() + " : "+ msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
