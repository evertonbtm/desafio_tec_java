package br.com.batista.desafio01.service.user;

import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.dto.user.UserSearchDTO;
import br.com.batista.desafio01.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    User save(User user);

    UserDTO toDTO(User entity);

    List<UserDTO> toDTO(List<User> entityList);

    User toEntity(UserDTO dto);

    User toEntity(User user, UserDTO dto);

    User findByDocumentOrEmail(String document, String email) throws Exception;

    User createUpdate(UserDTO userDTO) throws Exception;

    void delete(String param) throws Exception;

    Page<UserDTO> search(UserSearchDTO search, Pageable pageable);
}
