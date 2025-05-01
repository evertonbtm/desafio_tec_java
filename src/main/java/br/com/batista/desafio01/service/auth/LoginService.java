package br.com.batista.desafio01.service.auth;

import br.com.batista.desafio01.configuration.message.MessageConfig;
import br.com.batista.desafio01.configuration.message.MessageService;
import br.com.batista.desafio01.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoginService implements UserDetailsService {

    private final IUserRepository userRepository;

    private final MessageService messageService;

    public LoginService(IUserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        br.com.batista.desafio01.model.entities.User user = userRepository.findUserByName(username);

        if (user != null) {
            return new User(user.getName(), new BCryptPasswordEncoder().encode(user.getPassword()), new ArrayList<>(0));
        }

        throw new UsernameNotFoundException(messageService.getMessage("user.notfound"));
    }

}
