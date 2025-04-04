package com.christian.roleservice.role_service.services;

import java.util.List;
import java.util.Optional;

import com.christian.roleservice.role_service.entities.Role;

public interface RoleService {
    
    Optional<Role> findByName(String name);

   List<Role> findRolesByIds(List<Long> roleIds);

}
