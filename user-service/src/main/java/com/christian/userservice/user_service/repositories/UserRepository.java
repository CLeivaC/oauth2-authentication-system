package com.christian.userservice.user_service.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.christian.userservice.user_service.entities.User;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUsername(String username);

}
