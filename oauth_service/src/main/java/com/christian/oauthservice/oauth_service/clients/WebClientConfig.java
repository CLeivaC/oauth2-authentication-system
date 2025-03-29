package com.christian.oauthservice.oauth_service.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient.Builder webClientUsers() {
        return WebClient.builder().baseUrl("http://localhost:8002/api/users");
    }

    @Bean
    WebClient.Builder webClientRoles() {
        return WebClient.builder().baseUrl("http://localhost:8005/api/roles");
    }

}
