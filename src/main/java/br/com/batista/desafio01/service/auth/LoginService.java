package br.com.batista.desafio01.service.auth;

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

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

        br.com.batista.desafio01.model.entities.User user = userRepository.findUserByName(username);

        if (user != null) {
            return new User(user.getName(), new BCryptPasswordEncoder().encode(user.getPassword()), new ArrayList<>(0));
        }
        throw new UsernameNotFoundException("User not found");
    }

}
