package org.financk.financk_backend.auth.api;

import lombok.extern.slf4j.Slf4j;
import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.models.AuthenticationDTO;
import org.financk.financk_backend.auth.service.AuthenticationService;
import org.financk.financk_backend.common.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final String LOG_TITLE = "[AuthenticationController] -";
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationDTO user) {
        log.info("{} Register request received", LOG_TITLE);
        boolean validated = AuthenticationRequestValidator.validateRegisterDTO(user);
        if (validated) {
            log.debug("{} Request is valid, proceeding to registering the user", LOG_TITLE);
            ServiceResult<AuthenticationResponse> serviceResult = authenticationService.registerFinancialUser(user);
            if (serviceResult.isSuccess()) {
                log.info("{} User registered successfully", LOG_TITLE);
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.CREATED);
            }
            if (serviceResult.getErrorCode() == 1) {
                log.info("{} Registration failed because email was already taken.", LOG_TITLE);
                return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.CONFLICT);
            }
        }
        return createBadRequestResponse();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationDTO user) {
        log.info("{} Login request received", LOG_TITLE);
        boolean validated = AuthenticationRequestValidator.validateLoginDTO(user);
        if (validated) {
            log.debug("{} Request is valid, proceeding to logging in the user", LOG_TITLE);
            ServiceResult<AuthenticationResponse> serviceResult = authenticationService.loginFinancialUser(user);
            if (serviceResult.isSuccess()) {
                log.info("{} Login successfully", LOG_TITLE);
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
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

    @PostMapping("/oauth")
    public ResponseEntity<AuthenticationResponse> oauthLogin(@RequestBody AuthenticationDTO user) {
        //TODO: OAuth User
        boolean validated = AuthenticationRequestValidator.validateLoginDTO(user);
        if (validated) {
            ServiceResult<AuthenticationResponse> serviceResult = authenticationService.loginFinancialUser(user);
            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
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
}
