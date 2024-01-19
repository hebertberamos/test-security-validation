package com.securityvalidation.demo.entities.enums;

public enum UserRole {

    ADMIN("admin"),

    USER("üser");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
