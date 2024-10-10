package org.financk.financk_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
public class FinanckBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanckBackendApplication.class, args);
    }

}
