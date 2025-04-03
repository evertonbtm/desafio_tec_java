package br.com.batista.desafio01.service;


import br.com.batista.desafio01.exception.FieldDuplicatedException;
import br.com.batista.desafio01.exception.UserNotFoundException;
import br.com.batista.desafio01.exception.UserTypeNotFoundException;
import br.com.batista.desafio01.model.dto.user.UserDTO;
import br.com.batista.desafio01.model.entities.User;
import br.com.batista.desafio01.model.entities.UserType;
import br.com.batista.desafio01.repository.IUserRepository;
import br.com.batista.desafio01.service.user.UserService;
import br.com.batista.desafio01.service.usertype.UserTypeService;
import br.com.batista.desafio01.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserTypeService userTypeService;

    @Mock
    IUserRepository IUserRepository;


    @BeforeEach
    public void init(){

        Mockito.mockitoSession().initMocks(this);

    }

    @Test
    public void when_create_user_then_success() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        User user = userService.toEntity(userDTO);

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findTypeUser()).thenReturn(userType);

        when(IUserRepository.save(Mockito.any(User.class))).thenReturn(user);

        User found = userService.createUpdate(userDTO);

        assertEquals(found.getName(), user.getName());

    }

    @Test
    public void when_create_user_then_fail() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findTypeUser()).thenReturn(userType);

        when(IUserRepository.save(Mockito.any(User.class))).thenReturn(new User());

        User found = userService.createUpdate(userDTO);

        assertNotEquals(found.getName(), userDTO.getName());

    }

    @Test
    public void when_delete_user_then_userNotFoundException() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        String invalidParam = "invalidParam";

        assertThrows(UserNotFoundException.class, () -> userService.remove(invalidParam));
    }

    @Test
    public void when_delete_user_then_success() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findTypeUser()).thenReturn(userType);

        when(IUserRepository.save(Mockito.any(User.class))).thenReturn(new User());

        User found = userService.createUpdate(userDTO);

        String paramDelete = userDTO.getDocument();

        when(IUserRepository.findListByDocumentOrEmail(paramDelete, paramDelete))
                .thenReturn(Collections.singletonList(found));

        assertDoesNotThrow( () -> userService.remove(paramDelete));
    }

    @Test
    public void when_create_user_then_return_FieldDuplicatedException() throws Exception {

        List<User> duplicatedList= new ArrayList<>(0);
        UserDTO userDTO = new UserDTO();
        userDTO.setName("user1");
        userDTO.setPassword("user123!@#");
        userDTO.setEmail("user1@teste.com");
        userDTO.setDocument("012345678910");

        User user = userService.toEntity(userDTO);

        duplicatedList.add(user);
        duplicatedList.add(user);

        when(IUserRepository.findListByDocumentOrEmail(userDTO.getDocument(), userDTO.getEmail())).thenReturn(duplicatedList);

        assertThrows(FieldDuplicatedException.class, () -> userService.createUpdate(userDTO));

    }

    @Test
    public void when_update_user_then_success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("updatedUser");
        userDTO.setPassword("updatedPassword123!@#");
        userDTO.setEmail("updatedUser@teste.com");
        userDTO.setDocument("012345678910");

        User user = userService.toEntity(userDTO);
        user.setIdUser(1L);

        UserType userType = MockUtils.mockUserType();
        when(userTypeService.findTypeUser()).thenReturn(userType);

        when(IUserRepository.save(Mockito.any(User.class))).thenReturn(user);

        User updatedUser = userService.createUpdate(userDTO);

        assertEquals(updatedUser.getName(), userDTO.getName());
        assertEquals(updatedUser.getEmail(), userDTO.getEmail());
    }

    @Test
    public void when_update_user_then_userNotFoundException() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("updatedUser");
        userDTO.setPassword("updatedPassword123!@#");
        userDTO.setEmail("updatedUser@teste.com");
        userDTO.setDocument("012345678910");

        assertThrows(UserTypeNotFoundException.class, () -> userService.createUpdate(userDTO));
    }

    @Test
    public void when_find_user_by_id_then_success() throws Exception {
        User user = new User();
        user.setIdUser(1L);
        user.setName("user1");
        user.setEmail("user1@teste.com");

        when(IUserRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User foundUser = userService.findById(1L);

        assertEquals(foundUser.getName(), user.getName());
        assertEquals(foundUser.getEmail(), user.getEmail());
    }

    @Test
    public void when_find_user_by_id_then_userNotFoundException() throws Exception {
        when(IUserRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    public void when_create_user_with_invalid_data_then_InvalidUserException() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("");
        userDTO.setPassword("short");
        userDTO.setEmail("invalidEmail");
        userDTO.setDocument("012345678910");

        assertThrows(Exception.class, () -> userService.createUpdate(userDTO));
    }

}

