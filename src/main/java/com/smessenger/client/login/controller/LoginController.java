package com.smessenger.client.login.controller;

import com.smessenger.client.login.dao.LoginResponse;
import com.smessenger.client.shared.controller.MainController;
import com.smessenger.client.shared.dao.MainResponse;
import com.smessenger.client.shared.service.HttpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController extends MainController {

    private final HttpClientService httpClientService;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String requestLink;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @PostMapping("/login")
    public Mono<MainResponse> login() {
        final String requestUri = "/protocol/openid-connect/token";
        final String grantType = "password";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", grantType);
        requestBody.add("username", "john");
        requestBody.add("password", "password1");

        Mono<LoginResponse> loginResponseMono = httpClientService.postRequest(requestLink, requestUri, MediaType.APPLICATION_FORM_URLENCODED, null, requestBody);

        return loginResponseMono.map(
                loginResponse ->
                        new MainResponse()
                                .setTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                                .setPath("/api/v1/login")
                                .setStatus(200)
                                .setError(null)
                                .setBody(loginResponse)
        );
    }
}
