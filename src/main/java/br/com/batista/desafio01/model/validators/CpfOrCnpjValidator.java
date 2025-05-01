package br.com.batista.desafio01.model.validators;

import br.com.batista.desafio01.model.annotations.CpfOrCnpj;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfOrCnpjValidator implements ConstraintValidator<CpfOrCnpj, String> {

    private static final String CPF_REGEX = "\\d{11}";
    private static final String CNPJ_REGEX = "\\d{14}";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value.matches(CPF_REGEX) || value.matches(CNPJ_REGEX);
    }
}