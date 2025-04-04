package com.christian.userservice.user_service.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class InternalRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(InternalRequestFilter.class);

    // Secret key for signing the header shared only between `users` and `oauth`
    private static final String SECRET_KEY = System.getenv("SECRET_KEY");
    private static final String INTERNAL_HEADER = "X-Internal-Secret";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        logger.info("Requested URI: {}", requestUri); 

       // Block access to /api/users/username/{username} except for `oauth`
        if (requestUri.matches("^/api/users/username/[^/]+$")) {
            String providedSignature = request.getHeader(INTERNAL_HEADER);
            if(providedSignature==null){
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Firma inválida.");
                return;
            }
           
            if (!isValidSignature(providedSignature)) {
                logger.warn("Invalid signature, returning 403 Forbidden");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Firma inválida.");
                return;
            }
        }
       // If all validations pass, allow the request
        logger.info("Request passed validation, proceeding with filter chain");
        filterChain.doFilter(request, response);
    }

   // Signature validation using HMAC-SHA256
    private boolean isValidSignature(String providedSignature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"));
            String expectedSignature = Base64.getEncoder().encodeToString(mac.doFinal("oauth-service".getBytes()));

            return expectedSignature.equals(providedSignature);
        } catch (Exception e) {
            return false;
        }
    }

}
