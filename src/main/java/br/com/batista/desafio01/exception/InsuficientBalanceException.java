package br.com.batista.desafio01.exception;

import br.com.batista.desafio01.exception.base.ApiInternalServerErrorException;

public class InsuficientBalanceException extends ApiInternalServerErrorException {

    public InsuficientBalanceException(Class clazz, String field, String balance){
        super(clazz.getSimpleName() + " : " + field + " nao existe saldo suficiente "+balance);
    }
}
