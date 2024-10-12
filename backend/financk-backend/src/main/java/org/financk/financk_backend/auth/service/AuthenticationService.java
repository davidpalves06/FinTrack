package org.financk.financk_backend.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
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
    public static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 6;
    public static final long REFRESH_REMEMBER_ME_EXPIRATION_TIME = 1000L * 60 * 1440 * 30;
    private final FinancialUserRepository financialUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public AuthenticationService(FinancialUserRepository financialUserRepository, JWTUtils jwtUtils) {
        this.financialUserRepository = financialUserRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }


    public ServiceResult<AuthenticationResponse> registerFinancialUser(AuthenticationDTO userDTO) {
        log.debug("{} Registering user with email {}", LOG_TITLE,userDTO.getEmail());
        String password = userDTO.getPassword();
        String encodedPassword = this.passwordEncoder.encode(password);
        FinancialUser financialUser = new FinancialUser();
        financialUser.setPassword(encodedPassword);
        log.error(encodedPassword);
        financialUser.setAge(userDTO.getAge());
        financialUser.setEmail(userDTO.getEmail());
        financialUser.setName(userDTO.getName());
        if (!financialUserRepository.existsByEmail(financialUser.getEmail())) {
            log.debug("{} Saving user in database", LOG_TITLE);
            financialUserRepository.save(financialUser);
            return new ServiceResult<>(true,new AuthenticationResponse("User registered."),null,0);
        }
        log.debug("{} Could not register user because email was already taken", LOG_TITLE);
        return new ServiceResult<>(false,null,"Email already taken",1);
    }

    public ServiceResult<AuthenticationResponse> loginFinancialUser(AuthenticationDTO userDTO) {
        log.debug("{} Checking if user exists", LOG_TITLE);
        Optional<FinancialUser> financialUserOptional = financialUserRepository.findByEmail(userDTO.getEmail());
        if (financialUserOptional.isPresent()) {
            log.debug("{} User exists", LOG_TITLE);
            FinancialUser financialUser = financialUserOptional.get();
            if (passwordEncoder.matches(userDTO.getPassword(), financialUser.getPassword())) {
                log.debug("{} Password matches", LOG_TITLE);
                String refreshToken;
                log.debug("{} Generating Refresh Token", LOG_TITLE);
                if (userDTO.isRememberMe()) {
                    refreshToken = jwtUtils.createRefreshToken(financialUser.getEmail(),REFRESH_REMEMBER_ME_EXPIRATION_TIME);
                }
                else refreshToken = jwtUtils.createRefreshToken(financialUser.getEmail(), REFRESH_EXPIRATION_TIME);
                log.debug("{} Generating Access Token", LOG_TITLE);
                String accessToken = jwtUtils.createAccessToken(financialUser.getEmail());
                return new ServiceResult<>(true,new AuthenticationResponse(accessToken,"User logged in.",refreshToken),null,0);
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

    public ServiceResult<AuthenticationResponse> recoverPassword(AuthenticationDTO user) {
        //TODO: Recover password LOGIC
        return null;
    }


}
