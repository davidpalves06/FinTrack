package org.financk.financk_backend.auth.service;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.models.AuthenticationResult;
import org.financk.financk_backend.auth.models.FinancialUser;
import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.common.ServiceResult;
import org.financk.financk_backend.auth.security.jwt.JWTUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationService {
    private static final String LOG_TITLE = "[AuthenticationService] -";

    private final FinancialUserRepository financialUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public AuthenticationService(FinancialUserRepository financialUserRepository, JWTUtils jwtUtils) {
        this.financialUserRepository = financialUserRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }


    public ServiceResult<AuthenticationResult> registerFinancialUser(AuthenticationDTO userDTO) {
        log.debug("{} Registering user with email {}", LOG_TITLE,userDTO.getEmail());
        String password = userDTO.getPassword();
        String encodedPassword = this.passwordEncoder.encode(password);
        FinancialUser financialUser = new FinancialUser();
        financialUser.setPassword(encodedPassword);
        financialUser.setUsername(userDTO.getUsername());
        financialUser.setEmail(userDTO.getEmail());
        String[] fullNameString = userDTO.getName().trim().split(" ");
        financialUser.setName(fullNameString[0] + fullNameString[fullNameString.length - 1]);
        if (!financialUserRepository.existsByEmail(financialUser.getEmail())) {
            if (financialUserRepository.existsByUsername(financialUser.getUsername())) {
                return new ServiceResult<>(false,null,"Username already taken",2);
            }
            log.debug("{} Saving user in database", LOG_TITLE);
            financialUserRepository.save(financialUser);
            return new ServiceResult<>(true,new AuthenticationResult("User registered."),null,0);
        }
        log.debug("{} Could not register user because email was already taken", LOG_TITLE);
        return new ServiceResult<>(false,null,"Email already taken",1);
    }

    public ServiceResult<AuthenticationResult> loginFinancialUser(AuthenticationDTO userDTO) {
        log.debug("{} Checking if user exists", LOG_TITLE);
        Optional<FinancialUser> financialUserOptional = financialUserRepository.findByEmail(userDTO.getEmail());
        if (financialUserOptional.isPresent()) {
            log.debug("{} User exists", LOG_TITLE);
            FinancialUser financialUser = financialUserOptional.get();
            if (passwordEncoder.matches(userDTO.getPassword(), financialUser.getPassword())) {
                log.debug("{} Password matches", LOG_TITLE);
                log.debug("{} Generating Access Token", LOG_TITLE);
                String accessToken = jwtUtils.createAccessToken(financialUser.getEmail());
                AuthenticationDTO userInfo = new AuthenticationDTO();
                userInfo.setEmail(financialUser.getEmail());
                userInfo.setUsername(financialUser.getUsername());
                userInfo.setName(financialUser.getName());
                return new ServiceResult<>(true,new AuthenticationResult(accessToken,"User logged in.",userInfo),null,0);
            }
            else {
                log.debug("{} Password does not match", LOG_TITLE);
                return new ServiceResult<>(false,null,"Wrong password",1);
            }
        }
        else {
            log.debug("{} User does not exist", LOG_TITLE);
            return new ServiceResult<>(false,null,"User not found",2);
        }
    }

    public ServiceResult<AuthenticationResult> recoverPassword(AuthenticationDTO user) {
        //TODO: Recover password LOGIC
        return null;
    }


    public ServiceResult<AuthenticationResult> checkAuthentication(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("authToken")) {
                String token = cookie.getValue();
                Boolean validated = jwtUtils.validateToken(token);
                if (validated) {
                    String email = jwtUtils.extractEmail(token);
                    Optional<FinancialUser> financialUserOptional = financialUserRepository.findByEmail(email);
                    if (financialUserOptional.isPresent()) {
                        FinancialUser financialUser = financialUserOptional.get();
                        String accessToken = jwtUtils.createAccessToken(email);
                        AuthenticationDTO userInfo = new AuthenticationDTO();
                        userInfo.setEmail(email);
                        userInfo.setUsername(financialUser.getUsername());
                        userInfo.setName(financialUser.getName());
                        return new ServiceResult<>(true,new AuthenticationResult(accessToken,"User is authenticated.",userInfo),null,0);
                    }
                    else log.debug("{} Could not find user in JWT", LOG_TITLE);
                }
                else log.debug("{} JWT Token not valid", LOG_TITLE);
                return new ServiceResult<>(false,null,"User not authenticated",1);
            }
        }
        log.debug("{} No authentication cookie", LOG_TITLE);
        return new ServiceResult<>(false,null,"User not authenticated",1);
    }
}
