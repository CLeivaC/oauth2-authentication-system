package com.christian.roleservice.role_service.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.christian.roleservice.role_service.entities.Role;

public interface RoleRepository extends CrudRepository<Role,Long> {

    Optional<Role> findByName(String name);

}
