package com.christian.userservice.user_service.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.christian.userservice.user_service.clients.RoleFeignClient;
import com.christian.userservice.user_service.dto.RoleDto;
import com.christian.userservice.user_service.dto.UserDto;
import com.christian.userservice.user_service.dto.UserResponseDto;
import com.christian.userservice.user_service.entities.User;
import com.christian.userservice.user_service.exceptions.custom.UsernameNotFoundException;
import com.christian.userservice.user_service.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final RoleFeignClient roleFeignClient;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, RoleFeignClient roleFeignClient,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleFeignClient = roleFeignClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAll() {

        List<User> users = (List<User>) repository.findAll();

        List<UserResponseDto> usersResponse = users.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getRoleIds(),
                        user.getEnabled()))
                .collect(Collectors.toList());

        return usersResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuario con username '" + username + "' no encontrado"));
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
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);

        // Check if roles is null and assign an empty set if it is
        Set<Long> roleIds = Optional.ofNullable(userDto.getRoles())
                .orElse(new HashSet<>())
                .stream()
                .map(roleName -> {
                    // Call FeignClient to get the role by name
                    RoleDto role = roleFeignClient.getRoleByName(roleName);
                    return role.getId();
                })
                .collect(Collectors.toSet());
        // Ensure the "ROLE_USER" role is included
        Long userRoleId = roleFeignClient.getRoleByName("ROLE_USER").getId();
        roleIds.add(userRoleId); // Add the "ROLE_USER" role if not already present
        
        user.setRoleIds(roleIds);

        return repository.save(user);
    }

}
