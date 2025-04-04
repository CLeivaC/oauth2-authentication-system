package com.christian.oauthservice.oauth_service.services;

import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
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

    private static final String SECRET_KEY = System.getenv("SECRET_KEY");

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserService.class);

    // // Verificar si la clave secreta está obtenida correctamente
    // static {
    // if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
    // logger.error("La clave secreta no está configurada correctamente.");
    // } else {
    // logger.info("La clave secreta ha sido obtenida correctamente.");
    // }
    // }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        try {
            // Get the user
            User user = webClientUsers.build().get().uri("/username/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("X-Internal-Secret", generateSignature("oauth-service"))
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

            // Converts roles to GrantedAuthority to Spring Security
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

        // generate the HMAC-SHA256 signature
        private String generateSignature(String serviceName) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"));
                return Base64.getEncoder().encodeToString(mac.doFinal(serviceName.getBytes()));
            } catch (Exception e) {
                throw new RuntimeException("Error al generar la firma", e);
            }
        }

}
