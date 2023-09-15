package com.smessenger.client.login.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("expires_in")
    public int expiresIn;
    @JsonProperty("refresh_expires_in")
    public int refreshExpiresIn;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("not-before-policy")
    public int notBeforePolicy;
    @JsonProperty("session_state")
    public String sessionState;
    @JsonProperty("scope")
    public String scope;
}
