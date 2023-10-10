package com.smessenger.client.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@Slf4j
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults())
                )
                .authorizeExchange(
                        exchanges -> exchanges
                                .pathMatchers("/api/v1/health").permitAll()
                                .pathMatchers("/api/v1/login").permitAll()
                                .anyExchange().authenticated()
                );

        return httpSecurity.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("spring_security_roles");
            List<GrantedAuthority> authorities = roles.stream()
                    .filter(role -> role.startsWith("ROLE_"))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return Flux.fromIterable(authorities);
        });
        return converter;
    }
}