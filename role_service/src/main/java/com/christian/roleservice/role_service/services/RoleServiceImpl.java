package com.christian.roleservice.role_service.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.christian.roleservice.role_service.entities.Role;
import com.christian.roleservice.role_service.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository){
        this.repository = repository;
    }

    @Transactional
    @Override
    public Optional<Role> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Role> findRolesByIds(List<Long> roleIds) {
        Optional<List<Role>> rolesOptional = repository.findByIdIn(roleIds);

        if (rolesOptional.isPresent()) {
            return rolesOptional.get();
        } else {
            throw new RuntimeException("Roles not found for the provided IDs");
        }
    }

    

}
