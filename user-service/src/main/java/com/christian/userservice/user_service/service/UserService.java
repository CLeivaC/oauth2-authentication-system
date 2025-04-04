package com.christian.userservice.user_service.service;

import java.util.List;
import java.util.Optional;

import com.christian.userservice.user_service.dto.UserDto;
import com.christian.userservice.user_service.dto.UserResponseDto;
import com.christian.userservice.user_service.entities.User;

public interface UserService {

    List<UserResponseDto> findAll();
    Optional<User> findById(Long id);
    User findByUsername(String username);
    User assignRoleToUser(Long userId,String roleName);
    User save(UserDto userDto);

}
