package br.com.batista.desafio01.model.annotations;

import br.com.batista.desafio01.model.validators.CpfOrCnpjValidator;
import jakarta.validation.Constraint;
import org.springframework.messaging.handler.annotation.Payload;


import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CpfOrCnpjValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfOrCnpj {

    String message() default "Invalid CPF or CNPJ format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}