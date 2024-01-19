package com.securityvalidation.demo.repositories;

import com.securityvalidation.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByLogin(String login); // Retornando um UserDetails para que o Spring security o utilize

}
