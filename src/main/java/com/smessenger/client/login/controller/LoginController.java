package com.smessenger.client.login.controller;

import com.smessenger.client.login.dao.LoginRequestDao;
import com.smessenger.client.login.dao.LoginResponseDao;
import com.smessenger.client.shared.config.EnvironmentConfig;
import com.smessenger.client.shared.controller.MainController;
import com.smessenger.client.shared.dao.MainResponseDao;
import com.smessenger.client.shared.service.HttpClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController extends MainController {

    private final HttpClientService httpClientService;
    private final EnvironmentConfig environmentConfig;

    @PostMapping("/login")
    public Mono<MainResponseDao> login(@Valid @RequestBody LoginRequestDao loginRequestDao) {
        final String requestUri = "/protocol/openid-connect/token";
        final String grantType = "password";

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", environmentConfig.clientId);
        requestBody.add("client_secret", environmentConfig.clientSecret);
        requestBody.add("grant_type", grantType);
        requestBody.add("username", loginRequestDao.getUsername());
        requestBody.add("password", loginRequestDao.getPassword());

        Mono<LoginResponseDao> loginResponseMono = httpClientService.postRequest(environmentConfig.requestLink, requestUri, MediaType.APPLICATION_FORM_URLENCODED, null, requestBody);

        return loginResponseMono.map(
                loginResponse ->
                        new MainResponseDao()
                                .setTimestamp(Timestamp.valueOf(LocalDateTime.now()))
                                .setPath("/api/v1/login")
                                .setStatus(200)
                                .setError(null)
                                .setBody(loginResponse)
        );
    }
}
