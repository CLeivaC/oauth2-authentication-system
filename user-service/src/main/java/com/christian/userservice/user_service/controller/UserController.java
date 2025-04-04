package com.christian.userservice.user_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.christian.userservice.user_service.dto.UserDto;
import com.christian.userservice.user_service.dto.UserResponseDto;
import com.christian.userservice.user_service.entities.User;
import com.christian.userservice.user_service.service.UserService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.save(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/{userId}/asign-role/{roleName}")
    public ResponseEntity<User> assignRole(@PathVariable Long userId, @PathVariable String roleName) {
        User user = userService.assignRoleToUser(userId, roleName);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

}
