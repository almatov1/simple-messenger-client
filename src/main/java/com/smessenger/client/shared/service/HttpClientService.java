package com.smessenger.client.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HttpClientService {

    private final WebClient.Builder webClientBuilder;

    public Mono<HttpStatusCode> postRequest(String requestLink, String requestUri, MediaType mediaType, String authToken, Object body) {
        WebClient webClient = webClientBuilder.baseUrl(requestLink).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set("Authorization", authToken);

        return webClient
                .post()
                .uri(requestUri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .toBodilessEntity()
                .map(ResponseEntity::getStatusCode);
    }
}
