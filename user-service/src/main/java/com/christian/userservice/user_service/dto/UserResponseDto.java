package com.christian.userservice.user_service.dto;

import java.util.Set;

public class UserResponseDto {

    private Long id;
    private String username;
    private Set<Long> roleIds;
    private boolean enabled;

    public UserResponseDto(Long id,String username, Set<Long> roleIds,boolean enabled) {
        this.id = id;
        this.username = username;
        this.roleIds = roleIds;
        this.enabled = enabled;
    }

    
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



    
}

