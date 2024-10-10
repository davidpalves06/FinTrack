package org.financk.financk_backend.auth.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AuthenticationResponse {
    private String token;
    private String message;

    public AuthenticationResponse(String message) {
        this.message = message;
        this.token = null;
    }

    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
