package org.financk.financk_backend.auth.service;

import org.financk.financk_backend.auth.models.FinancialUser;
import org.financk.financk_backend.auth.models.FinancialUserDTO;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.common.ServiceResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AuthenticationService {

    private final FinancialUserRepository financialUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(FinancialUserRepository financialUserRepository) {
        this.financialUserRepository = financialUserRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }


    public ServiceResult<String> registerFinancialUser(FinancialUserDTO userDTO) {
        String password = userDTO.getPassword();
        String encodedPassword = this.passwordEncoder.encode(password);
        FinancialUser financialUser = new FinancialUser();
        financialUser.setPassword(encodedPassword);
        financialUser.setAge(userDTO.getAge());
        financialUser.setEmail(userDTO.getEmail());
        financialUser.setName(userDTO.getName());
        financialUserRepository.save(financialUser);
        return new ServiceResult<>(true,"User registered.",null);
    }

    public ServiceResult<String> loginFinancialUser(FinancialUserDTO user) {
        //TODO: LOGIN LOGIC
        return new ServiceResult<>(true,"User logged in.",null);
    }

    public ServiceResult<String> recoverPassword(FinancialUserDTO user) {
        //TODO: Recover password LOGIC
        return null;
    }
}
