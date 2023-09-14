package com.smessenger.client.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.stream.Stream;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
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
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new JwtAuthenticationConverter();
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        converter.setPrincipalClaimName("preferred_username");
        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> {
                    var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
                    var roles = jwt.getClaimAsStringList("spring_security_roles");

                    return Stream.concat(
                            authorities.stream(),
                            roles.stream()
                                    .filter(role -> role.startsWith("ROLE_"))
                                    .map(SimpleGrantedAuthority::new)
                                    .map(GrantedAuthority.class::cast)
                    ).toList();
                }
        );

        return converter;
    }
}