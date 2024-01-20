package com.securityvalidation.demo.controller;

import com.securityvalidation.demo.config.TokenServiceConfig;
import com.securityvalidation.demo.dtos.AuthenticationDTO;
import com.securityvalidation.demo.dtos.LoginResponseDTO;
import com.securityvalidation.demo.dtos.RegisterDTO;
import com.securityvalidation.demo.entities.User;
import com.securityvalidation.demo.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenServiceConfig tokenService;

    // Endpoint de login de usuário
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto){
        // forma de validação para ver se o login e a senha se encontram cadastrados no banco de dad
        var userNamePassword = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var auth = this.authenticationManager.authenticate(userNamePassword); // Autenticaçào do usuário e da senha

        // Token para que o usuário possa utilizar nas próximas requisições.
         var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    // Endpoint de criação de um novo usuário
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO dto){
        if(repository.findByLogin(dto.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        User user = new User(dto.login(), encryptedPassword, dto.role());

        repository.save(user);
        return ResponseEntity.ok().build();
    }
}
