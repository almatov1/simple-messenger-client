package com.smessenger.client.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    public String requestLink;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    public String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    public String clientSecret;
}
