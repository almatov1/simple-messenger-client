package com.smessenger.client.login.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRequest {
    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("grant_type")
    private String grantType;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
