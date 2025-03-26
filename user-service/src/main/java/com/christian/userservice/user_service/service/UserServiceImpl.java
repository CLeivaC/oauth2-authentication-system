package com.christian.userservice.user_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.christian.userservice.user_service.clients.RoleFeignClient;
import com.christian.userservice.user_service.dto.RoleDto;
import com.christian.userservice.user_service.dto.UserDto;
import com.christian.userservice.user_service.entities.User;
import com.christian.userservice.user_service.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final RoleFeignClient roleFeignClient;

    public UserServiceImpl(UserRepository repository, RoleFeignClient roleFeignClient) {
        this.repository = repository;
        this.roleFeignClient = roleFeignClient;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional
    @Override
    public User assignRoleToUser(Long userId, String roleName) {

        RoleDto role = roleFeignClient.getRoleByName(roleName);

        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.getRoleIds().add(role.getId());

        return repository.save(user);

    }

    @Transactional
@Override
public User save(UserDto userDto) {
    User user = new User();
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());

    // Verificar si roles es null y asignar un conjunto vacío en caso de serlo
    Set<Long> roleIds = Optional.ofNullable(userDto.getRoles())
            .orElse(new HashSet<>()) // Si roles es null, asignar un conjunto vacío
            .stream()
            .map(roleName -> {
                // Llamada al FeignClient para obtener el role por nombre
                RoleDto role = roleFeignClient.getRoleByName(roleName);
                return role.getId();
            })
            .collect(Collectors.toSet());

    user.setRoleIds(roleIds);

    return repository.save(user);
}


}
