package com.securityvalidation.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.securityvalidation.demo.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// serviço que vai fazer a geração dos tokens
@Service
public class TokenServiceConfig {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")                   // Who created this Token (name that identifies the application)
                    .withSubject(user.getLogin())             // User who is receiving this token (save the user's login in the token so the system can identify who is the user who is receiving it)
                    .withExpiresAt(generateExpirationDate())  // Expiration time of this token
                    .sign(algorithm);                         // To make the signature and final generation
            return token;
        }
        catch (JWTCreationException e){
            throw new RuntimeException("Error while generated token " + e);
        }
    }

    // Método de validação do Token
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")  // Who was the sender
                    .build()                 // Reassembling the data inside the token
                    .verify(token)           // Token decryption
                    .getSubject();           // Captures the Subject that had been saved inside the token
        } catch(JWTVerificationException exception){
            return "";                       // Return an empty String because the method that needs this user and receives an empty String will already realize that I don't have any user (returns an unauthorized error)
        }
    }


    // Token expiration time 2 hours.
    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
