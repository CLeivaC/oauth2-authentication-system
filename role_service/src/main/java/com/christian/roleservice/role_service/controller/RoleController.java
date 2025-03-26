package com.christian.roleservice.role_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.christian.roleservice.role_service.entities.Role;
import com.christian.roleservice.role_service.services.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/roles")
@RestController
public class RoleController {

    
    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable String name) {
        Role role = roleService.findByName(name).orElseThrow(() -> new RuntimeException("Role not found"));
        return ResponseEntity.ok(role);
    }
    

}
