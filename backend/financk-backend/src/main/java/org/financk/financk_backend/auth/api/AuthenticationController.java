package org.financk.financk_backend.auth.api;

import org.financk.financk_backend.auth.models.FinancialUser;
import org.financk.financk_backend.auth.models.FinancialUserDTO;
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
    public ResponseEntity<String> registerUser(@RequestBody FinancialUserDTO user) {
        boolean validated = AuthenticationRequestValidator.validateRegisterDTO(user);
        if (validated) {
            ServiceResult<String> serviceResult = authenticationService.registerFinancialUser(user);
            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>("Could not register user", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody FinancialUserDTO user) {
        boolean validated = AuthenticationRequestValidator.validateLoginDTO(user);
        if (validated) {
            ServiceResult<String> serviceResult = authenticationService.loginFinancialUser(user);
            if (serviceResult.isSuccess()) {
                return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Login failed.",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestBody FinancialUserDTO user) {
        //TODO: VALIDATE NECESSARY INFO AND HOW TO HANDLE THIS SCENARIO
        ServiceResult<String> serviceResult = authenticationService.recoverPassword(user);
        if (serviceResult.isSuccess()) {
            return new ResponseEntity<>(serviceResult.getData(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(serviceResult.getError(), HttpStatus.BAD_REQUEST);
        }
    }
}
