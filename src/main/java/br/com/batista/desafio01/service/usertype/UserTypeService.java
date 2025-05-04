package br.com.batista.desafio01.service.usertype;

import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.model.entities.UserType;
import br.com.batista.desafio01.model.enums.EUserType;
import br.com.batista.desafio01.repository.IUserTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService implements IUserTypeService {

    private final IUserTypeRepository userTypeRepository;

    public UserTypeService(IUserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public UserType save(UserType userType){
        return userTypeRepository.save(userType);
    }

    @Override
    public UserType findByType(String type) throws FieldDuplicatedException {
        List<UserType> userTypeList = userTypeRepository.findListByType(type);

        if(userTypeList == null || userTypeList.isEmpty()){
            return null;
        }

        if(userTypeList.size() > 1){
            throw new FieldDuplicatedException(UserType.class, "type", type);
        }

        return userTypeList.get(0);
    }

    public UserType findTypeShopkeeper() throws FieldDuplicatedException {
        return findByType(EUserType.SHOPKEEPER.get());
    }

    public UserType findTypeUser() throws FieldDuplicatedException {
        return findByType(EUserType.USER.get());
    }

}
