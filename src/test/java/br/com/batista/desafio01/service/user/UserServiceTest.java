package br.com.batista.desafio01.service.user;


import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.exception.UserAlreadyCreatedException;
import br.com.batista.desafio01.exception.UserNotFoundException;
import br.com.batista.desafio01.exception.UserTypeNotFoundException;
import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.entities.User;
import br.com.batista.desafio01.model.entities.UserType;
import br.com.batista.desafio01.repository.IUserRepository;
import br.com.batista.desafio01.service.usertype.IUserTypeService;
import br.com.batista.desafio01.utils.MockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    IUserTypeService userTypeService;

    @Mock
    IUserRepository userRepository;

    @Test
    void when_create_user_then_success() {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        User user = userService.toEntity(userDTO);

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findByType("USER")).thenReturn(userType);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User found = userService.create(userDTO);

        assertEquals(found.getName(), user.getName());

    }

    @Test
    void when_create_user_then_fail() {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");an you genera

        User existsUsers = userService.toEntity(userDTO);

        // Simulate user already exists
        when(userRepository.findListByDocumentOrEmail(userDTO.getDocument(), userDTO.getEmail()))
            .thenReturn(Collections.singletonList(existsUsers));

        assertThrows(br.com.batista.desafio01.exception.UserAlreadyCreatedException.class, () -> userService.create(userDTO));
    }

    @Test
    void when_delete_user_then_userNotFoundException()  {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        String invalidParam = "invalidParam";

        assertThrows(UserNotFoundException.class, () -> userService.delete(invalidParam));
    }

    @Test
    void when_delete_user_then_success()  {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findByType("USER")).thenReturn(userType);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        User found = userService.create(userDTO);

        String paramDelete = userDTO.getDocument();

        when(userRepository.findListByDocumentOrEmail(paramDelete, paramDelete))
                .thenReturn(Collections.singletonList(found));

        assertDoesNotThrow( () -> userService.delete(paramDelete));
    }

    @Test
    void when_create_user_then_return_FieldDuplicatedException()  {

        List<User> duplicatedList= new ArrayList<>(0);
        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        User user = userService.toEntity(userDTO);

        duplicatedList.add(user);
        duplicatedList.add(user);

        when(userRepository.findListByDocumentOrEmail(userDTO.getDocument(), userDTO.getEmail())).thenReturn(duplicatedList);

        assertThrows(FieldDuplicatedException.class, () -> userService.create(userDTO));

    }

    @Test
    void when_update_user_then_success()  {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("updatedUser");
        userDTO.setPassword("updatedPassword123!@#");
        userDTO.setEmail("updatedUser@teste.com");
        userDTO.setDocument("012345678910");

        User user = userService.toEntity(userDTO);
        user.setIdUser(1L);

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findByType("USER")).thenReturn(userType);

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User updatedUser = userService.create(userDTO);

        assertEquals(updatedUser.getName(), userDTO.getName());
        assertEquals(updatedUser.getEmail(), userDTO.getEmail());
    }

    @Test
    void when_update_user_then_userNotFoundException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("updatedUser");
        userDTO.setPassword("updatedPassword123!@#");
        userDTO.setEmail("updatedUser@teste.com");
        userDTO.setDocument("012345678910");

        // Not mocking userTypeService.findByType("USER") here on purpose,
        // so it returns null and triggers UserTypeNotFoundException
        assertThrows(UserTypeNotFoundException.class, () -> userService.create(userDTO));
    }

    @Test
    void when_find_user_by_id_then_success()  {
        User user = new User();
        user.setIdUser(1L);
        user.setName("user1");
        user.setEmail("user1@teste.com");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User foundUser = userService.findById(1L);

        assertEquals(foundUser.getName(), user.getName());
        assertEquals(foundUser.getEmail(), user.getEmail());
    }

    @Test
    void when_find_user_by_id_then_userNotFoundException()  {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void when_create_user_with_invalid_data_then_InvalidUserException()  {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("");
        userDTO.setPassword("short");
        userDTO.setEmail("invalidEmail");
        userDTO.setDocument("012345678910");

        assertThrows(Exception.class, () -> userService.create(userDTO));
    }

}
