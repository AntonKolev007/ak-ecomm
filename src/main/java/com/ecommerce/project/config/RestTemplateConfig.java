package com.ecommerce.project.config;

import com.ecommerce.project.security.jwt.JwtInterceptor;
import com.ecommerce.project.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    private final JwtUtils jwtUtils;

    public RestTemplateConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public RestTemplate restTemplate() {
        // Fetch or generate the JWT token
        String token = jwtUtils.generateTokenFromUsername("yourUsername"); // Replace "yourUsername" with the actual username or method to fetch the token

        RestTemplate restTemplate = new RestTemplate();

        // Add the interceptor with the token
        JwtInterceptor jwtInterceptor = new JwtInterceptor(token);

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors.isEmpty()) {
            restTemplate.setInterceptors(Collections.singletonList(jwtInterceptor));
        } else {
            interceptors.add(jwtInterceptor);
            restTemplate.setInterceptors(interceptors);
        }

        return restTemplate;
    }
}
