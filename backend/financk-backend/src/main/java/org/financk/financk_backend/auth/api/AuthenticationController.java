package org.financk.financk_backend.auth.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.models.AuthenticationResult;
import org.financk.financk_backend.auth.security.jwt.JWTUtils;
import org.financk.financk_backend.auth.service.AuthenticationService;
import org.financk.financk_backend.common.ServiceResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final String LOG_TITLE = "[AuthenticationController] -";
    public static final int AUTH_COOKIE_EXPIRY = 60 * 60 * 24 * 30;
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationDTO authDTO) {
        log.info("{} Register request received", LOG_TITLE);
        boolean validated = AuthenticationRequestValidator.validateRegisterDTO(authDTO);
        if (validated) {
            log.debug("{} Request is valid, proceeding to registering the user", LOG_TITLE);
            ServiceResult<AuthenticationResult> serviceResult = authenticationService.registerFinancialUser(authDTO);
            if (serviceResult.isSuccess()) {
                log.info("{} User registered successfully", LOG_TITLE);
                AuthenticationResult authenticationResult = serviceResult.getData();
                return new ResponseEntity<>(new AuthenticationResponse(authenticationResult.getMessage(), authenticationResult.getUser()), HttpStatus.CREATED);
            }
            if (serviceResult.getErrorCode() == 1) {
                log.info("{} Registration failed because email was already taken.", LOG_TITLE);
                return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.CONFLICT);
            }
            if (serviceResult.getErrorCode() == 2) {
                log.info("{} Registration failed because username was already taken.", LOG_TITLE);
                return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.CONFLICT);
            }
        }
        return createBadRequestResponse();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationDTO authDTO, HttpServletResponse response) {
        log.info("{} Login request received", LOG_TITLE);
        boolean validated = AuthenticationRequestValidator.validateLoginDTO(authDTO);
        if (validated) {
            log.debug("{} Request is valid, proceeding to logging in the user", LOG_TITLE);
            ServiceResult<AuthenticationResult> serviceResult = authenticationService.loginFinancialUser(authDTO);
            if (serviceResult.isSuccess()) {
                Cookie authCookie = createAuthCookie(serviceResult);
                response.addCookie(authCookie);
                log.info("{} Login successfully", LOG_TITLE);
                AuthenticationResult authenticationResult = serviceResult.getData();
                return new ResponseEntity<>(new AuthenticationResponse(authenticationResult.getMessage(),authenticationResult.getUser()), HttpStatus.OK);
            }
            if (serviceResult.getErrorCode() == 1) {
                log.info("{} Unauthorized access. Login failed", LOG_TITLE);
                return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.UNAUTHORIZED);
            }
            else if (serviceResult.getErrorCode() == 2) {
                log.info("{} Could not found user. Login failed", LOG_TITLE);
                return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.NOT_FOUND);
            }
        }
        return createBadRequestResponse();
    }

    @GetMapping("/check")
    public ResponseEntity<AuthenticationResponse> checkAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.info("{} Check authentication request received", LOG_TITLE);
        ServiceResult<AuthenticationResult> serviceResult = authenticationService.checkAuthentication(request.getCookies());
        if (serviceResult.isSuccess()) {
            log.info("{} User is authenticated", LOG_TITLE);
            Cookie authCookie = createAuthCookie(serviceResult);
            response.addCookie(authCookie);
            AuthenticationResult authenticationResult = serviceResult.getData();
            return new ResponseEntity<>(new AuthenticationResponse(authenticationResult.getMessage(),authenticationResult.getUser()), HttpStatus.OK);
        }
        log.info("{} User is not authenticated", LOG_TITLE);
        return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }



    @PostMapping("/oauth")
    public ResponseEntity<AuthenticationResponse> oauthLogin(@RequestBody AuthenticationDTO authDTO) {
        //TODO: OAuth User
        boolean validated = AuthenticationRequestValidator.validateLoginDTO(authDTO);
        if (validated) {
            ServiceResult<AuthenticationResult> serviceResult = authenticationService.loginFinancialUser(authDTO);
            if (serviceResult.isSuccess()) {
                AuthenticationResult authenticationResult = serviceResult.getData();
                return new ResponseEntity<>(new AuthenticationResponse(authenticationResult.getMessage(),authenticationResult.getUser()), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new AuthenticationResponse("Request Malformed"),HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<AuthenticationResponse> recoverPassword(@RequestBody AuthenticationDTO user) {
        //TODO: HANDLE THIS SCENARIO
//        ServiceResult<AuthenticationResponse> serviceResult = authenticationService.recoverPassword(user);
//        if (serviceResult.isSuccess()) {
//            return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.BAD_REQUEST);
//        }

        return new ResponseEntity<>(new AuthenticationResponse("OK"), HttpStatus.OK);
    }

    private ResponseEntity<AuthenticationResponse> createBadRequestResponse() {
        log.info("{} Request was not valid.", LOG_TITLE);
        return new ResponseEntity<>(new AuthenticationResponse("Request Malformed"), HttpStatus.BAD_REQUEST);
    }

    private Cookie createAuthCookie(ServiceResult<AuthenticationResult> serviceResult) {
        AuthenticationResult authenticationResult = serviceResult.getData();
        String accessToken = authenticationResult.getAccessToken();
        Cookie authCookie = new Cookie("authToken",accessToken);
        authCookie.setHttpOnly(true);
        //TODO: HANDLE ONLY HTTPS TRAFFIC
//                authCookie.setSecure(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(AUTH_COOKIE_EXPIRY);
        authCookie.setAttribute("SameSite","Lax");
        return authCookie;
    }
}
