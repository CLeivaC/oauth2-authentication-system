package com.christian.userservice.user_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.christian.userservice.user_service.dto.RoleDto;

@FeignClient(name = "role-service", url = "http://localhost:8005/api/roles")
public interface RoleFeignClient {

    @GetMapping("/name/{name}")
    RoleDto getRoleByName(@PathVariable String name);

}
