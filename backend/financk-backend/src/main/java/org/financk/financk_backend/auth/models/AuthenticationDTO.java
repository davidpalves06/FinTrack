package org.financk.financk_backend.auth.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {
    private String name;
    private String email;
    private String password;
    private String username;
}
