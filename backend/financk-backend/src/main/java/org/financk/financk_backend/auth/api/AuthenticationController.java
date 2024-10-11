package org.financk.financk_backend.auth.api;

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

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody AuthenticationDTO user) {
        boolean validated = AuthenticationRequestValidator.validateRegisterDTO(user);
        if (validated) {
            ServiceResult<AuthenticationResponse> serviceResult = authenticationService.registerFinancialUser(user);
            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(new AuthenticationResponse("Request Malformed"), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationDTO user) {
        boolean validated = AuthenticationRequestValidator.validateLoginDTO(user);
        if (validated) {
            ServiceResult<AuthenticationResponse> serviceResult = authenticationService.loginFinancialUser(user);
            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new AuthenticationResponse("Request Malformed"),HttpStatus.BAD_REQUEST);
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
        //TODO: VALIDATE NECESSARY INFO AND HOW TO HANDLE THIS SCENARIO
        ServiceResult<AuthenticationResponse> serviceResult = authenticationService.recoverPassword(user);
        if (serviceResult.isSuccess()) {
            return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new AuthenticationResponse(serviceResult.getErrorMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
