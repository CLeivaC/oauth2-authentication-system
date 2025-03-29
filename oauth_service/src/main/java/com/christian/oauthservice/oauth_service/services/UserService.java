package com.christian.oauthservice.oauth_service.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.christian.oauthservice.oauth_service.models.Role;
import com.christian.oauthservice.oauth_service.models.User;

@Service
public class UserService implements UserDetailsService {

    private WebClient.Builder webClientUsers;

    private WebClient.Builder webClientRoles;

    public UserService(@Qualifier("webClientUsers") WebClient.Builder webClientUsers,
            @Qualifier("webClientRoles") WebClient.Builder webClientRoles) {
        this.webClientUsers = webClientUsers;
        this.webClientRoles = webClientRoles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        try {
            // Get the user
            User user = webClientUsers.build().get().uri("/username/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();

            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            // Check if roleIds is not null
            if (user.getRoleIds() == null || user.getRoleIds().isEmpty()) {
                throw new RuntimeException("No roles found for user: " + username);
            }

            // Get the IDs of the roles the user has
            Set<Long> roleIds = new HashSet<>(user.getRoleIds());


            // make the request to WebClient to obtain the roles using the roleIds 
            List<Role> roles = webClientRoles.build().get()
                    .uri(uriBuilder -> uriBuilder.path("/ids")
                            .queryParam("ids",
                                    String.join(",",
                                            roleIds.stream().map(String::valueOf).collect(Collectors.toList())))
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Role>>() {
                    })
                    .block();

            if (roles == null || roles.isEmpty()) {
                throw new RuntimeException("Roles not found for user: " + username);
            }

            //Converts roles to GrantedAuthority to Spring Security
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            //// Create and return the UserDetails object of spring security
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    user.isEnabled(), true, true, true, authorities);

        } catch (WebClientResponseException e) {
            throw new UsernameNotFoundException("Error en el login: " + e.getMessage(), e);
        } catch (WebClientException e) {
            throw new UsernameNotFoundException("Error de comunicación con el servicio de usuarios: " + e.getMessage(),
                    e);
        }
    }

}
