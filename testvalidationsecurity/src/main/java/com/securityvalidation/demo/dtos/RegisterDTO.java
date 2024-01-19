package com.securityvalidation.demo.dtos;

import com.securityvalidation.demo.entities.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
