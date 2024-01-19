package com.securityvalidation.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Fala para o spring os méotod dessa classe como padrão de configuração das requisições
public class SecurityConfig {

    // Desabilita as configurações padrão do Spring Security
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //fazer machs com as URLs que estão sendo chamadas pra ver qual regra eu quero seguir para cada URL
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // só para teste
                        .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .build();
    }

    // Indicar para o Spring da onde a classe AuthenticationController tem que pegar o atributo AuthenticationManager que foi injetado dentro dela
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authentication) throws Exception{
        return authentication.getAuthenticationManager();
    }

    // Encriptar a senha do usuário para salvar no banco de dados.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
