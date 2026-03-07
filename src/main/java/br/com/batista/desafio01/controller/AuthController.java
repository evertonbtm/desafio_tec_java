package br.com.batista.desafio01.controller;

import br.com.batista.desafio01.configuration.auth.JwtAuthUtil;
import br.com.batista.desafio01.model.dto.auth.AuthDTO;
import br.com.batista.desafio01.model.dto.auth.AuthResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtAuthUtil jwtAuthUtil;

    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtAuthUtil jwtAuthUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtAuthUtil = jwtAuthUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtAuthUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}