package br.com.batista.desafio01.service.usertype;

import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.model.entities.UserType;

public interface IUserTypeService {

    UserType save(UserType userType);

    UserType findByType(String type) throws FieldDuplicatedException;

}
