package com.securityvalidation.demo.config;

import com.securityvalidation.demo.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {        // Este extends identifica para a classe que ele vai ser um filter que acontece uma vez a cada requisição

    @Autowired
    private TokenServiceConfig tokenService;

    @Autowired
    private UserRepository userRepository;


    // Method to take token and recover information inside it
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);
        if(token != null){
            var login = tokenService.validateToken(token);
            UserDetails user = userRepository.findByLogin(login);

            // User verification
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);     // As the user is already validated, save the user in the authentication context
        }

        filterChain.doFilter(request, response);                                      // Call next filter when token doesn't exist
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;                                          // This means that there is no token in the request

        return authHeader.replace("Bearer ", "");                  // Fazendo a substiruição do "Bearer " por um valor vazio para que seja passado apenas o toke, pois sempre que é feita a identificação esse nome vem, por padrão, na frente do valor do token.
    }
}
