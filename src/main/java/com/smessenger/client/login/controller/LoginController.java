package com.smessenger.client.login.controller;

import com.smessenger.client.shared.controller.MainController;
import com.smessenger.client.shared.service.HttpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public void login() {
        final String requestUri = "/protocol/openid-connect/token";
        final String grantType = "password";

//        LoginRequest loginRequest = new LoginRequest()
//                .setClientId(clientId)
//                .setClientSecret(clientSecret)
//                .setGrantType(grantType)
//                .setUsername("asd")
//                .setPassword("asd");

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", grantType);
        requestBody.add("username", "asd");
        requestBody.add("password", "asd");

        Mono<HttpStatusCode> httpStatusCodeMono = httpClientService.postRequest(requestLink, requestUri, MediaType.APPLICATION_FORM_URLENCODED, null, requestBody);

        return httpStatusCodeMono.
    }
}
