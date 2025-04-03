package br.com.batista.desafio01.service.user;

import br.com.batista.desafio01.configuration.message.MessageService;
import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.exception.UserDeletedException;
import br.com.batista.desafio01.exception.UserNotFoundException;
import br.com.batista.desafio01.exception.UserTypeNotFoundException;
import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.dto.user.UserSearchDTO;
import br.com.batista.desafio01.model.entities.User;
import br.com.batista.desafio01.repository.ITransactionRepository;
import br.com.batista.desafio01.repository.IUserRepository;
import br.com.batista.desafio01.service.usertype.IUserTypeService;
import br.com.batista.desafio01.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserTypeService userTypeService;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public UserDTO toDTO(User entity){
        return entity == null ? null : new UserDTO(entity);
    }

    @Override
    public List<UserDTO> toDTO(List<User> entityList){
        return entityList.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public Page<UserDTO> toDTO(Page<User> entityList){
        return entityList.map(this::toDTO);
    }

    @Override
    public User toEntity(UserDTO dto){
        return toEntity(new User(), dto);
    }

    @Override
    public User toEntity(User user, UserDTO dto){

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setDocument(dto.getDocument());
        user.setPassword(dto.getPassword());
        user.setMoneyBalance(dto.getMoneyBalance());

        return user;
    }

    @Override
    public void delete(String param) throws Exception {
        User user = findByDocumentOrEmail(param, param);

        if(user == null){
            throw new UserNotFoundException(User.class, "document or email", param);
        }

        user.setDeleted(true);
        save(user);
    }


    @Override
    @Transactional
    public User save(User user){
        return userRepository.save(user);
    }

    public User findByDocumentOrEmail(String document, String email) throws Exception {
        List<User> userList = userRepository.findListByDocumentOrEmail(document, email);

        if(userList == null || userList.isEmpty()){
            return null;
        }

        User user = userList.get(0);

        if(userList.size() > 1){
            throw new FieldDuplicatedException(User.class, "document or email", document);
        }

        if(!document.equals(email) && 
                !(user.getDocument().equals(document) && user.getEmail().equals(email))){
            throw new FieldDuplicatedException(User.class, "document or email", document);
        }

        if(user.isDeleted()){
            throw new UserDeletedException(User.class, "document or email", document);
        }

        return user;
    }


    @Override
    public User createUpdate(UserDTO userDTO) throws Exception {

        User user = findByDocumentOrEmail(userDTO.getDocument(), userDTO.getEmail());

        if(user == null){
            user = new User();
            user.setDocument(userDTO.getDocument());
            user.setEmail(userDTO.getEmail());
        }

        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setMoneyBalance(userDTO.getMoneyBalance() == null ? BigDecimal.ZERO : userDTO.getMoneyBalance());

        validateUserType(user);

        return save(user);
    }

    private void validateUserType(User user) throws Exception {
        user.setUserType(userTypeService.findTypeUser());

        if(ValidationUtils.isValidCpfOrCnpj(user.getDocument())
                && ValidationUtils.isValidCnpj(user.getDocument())){

            user.setUserType(userTypeService.findTypeShopkeeper());

        }

        if(user.getUserType() == null){
            throw new UserTypeNotFoundException(User.class, "userType");
        }
    }

    public User findById(long idUser) throws Exception {
        return userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException(User.class, "idUser", String.valueOf(idUser)));
    }

    public List<UserDTO> findAll() {
        return toDTO((List<User>) userRepository.findAll());
    }

    public Page<UserDTO> search(UserSearchDTO search, Pageable pageable) {
        Pageable sortedDefault = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() > 100 ? 20 : pageable.getPageSize());

        return toDTO(userRepository.findAll(search.getSpecification(), sortedDefault));
    }

}
