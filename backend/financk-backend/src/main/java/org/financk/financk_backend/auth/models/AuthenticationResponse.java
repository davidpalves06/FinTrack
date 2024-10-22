package org.financk.financk_backend.auth.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    private String message;
    private AuthenticationDTO user;

    public AuthenticationResponse(String message) {
        this.message = message;
    }

    public AuthenticationResponse(String message, AuthenticationDTO user) {
        this.message = message;
        this.user = user;
    }
}
