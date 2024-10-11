package org.financk.financk_backend.auth.models;

public class AuthenticationResponse {
    private String accessToken;
    private String message;
    private String refreshToken;

    public AuthenticationResponse(String message) {
        this.message = message;
        this.accessToken = null;
    }

    public AuthenticationResponse(String accessToken, String message,String refreshToken) {
        this.accessToken = accessToken;
        this.message = message;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
