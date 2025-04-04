package com.christian.userservice.user_service.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 3,message = "El username debe tener al menos 3 caracteres")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    
    private Set<String> roles;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    

}
