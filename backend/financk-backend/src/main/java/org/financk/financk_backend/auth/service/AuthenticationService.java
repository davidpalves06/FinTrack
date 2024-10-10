package org.financk.financk_backend.auth.service;

import org.financk.financk_backend.auth.models.AuthenticationResponse;
import org.financk.financk_backend.auth.models.FinancialUser;
import org.financk.financk_backend.auth.models.FinancialUserDTO;
import org.financk.financk_backend.auth.repository.FinancialUserRepository;
import org.financk.financk_backend.common.ServiceResult;
import org.financk.financk_backend.auth.security.jwt.JWTUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final FinancialUserRepository financialUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;

    public AuthenticationService(FinancialUserRepository financialUserRepository, JWTUtils jwtUtils) {
        this.financialUserRepository = financialUserRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
    }


    public ServiceResult<AuthenticationResponse> registerFinancialUser(FinancialUserDTO userDTO) {
        String password = userDTO.getPassword();
        String encodedPassword = this.passwordEncoder.encode(password);
        FinancialUser financialUser = new FinancialUser();
        financialUser.setPassword(encodedPassword);
        financialUser.setAge(userDTO.getAge());
        financialUser.setEmail(userDTO.getEmail());
        financialUser.setName(userDTO.getName());
        if (!financialUserRepository.existsByEmail(financialUser.getEmail())) {
            financialUserRepository.save(financialUser);
            return new ServiceResult<>(true,new AuthenticationResponse("User registered."),null,0);
        }
        return new ServiceResult<>(false,null,"Email already taken",1);
    }

    public ServiceResult<AuthenticationResponse> loginFinancialUser(FinancialUserDTO userDTO) {
        Optional<FinancialUser> financialUserOptional = financialUserRepository.findByEmail(userDTO.getEmail());
        if (financialUserOptional.isPresent()) {
            FinancialUser financialUser = financialUserOptional.get();
            if (passwordEncoder.matches(userDTO.getPassword(), financialUser.getPassword())) {
                //TODO : STUDY REFRESH TOKEN AND IMPLEMENT
                String jwtToken = jwtUtils.createToken(financialUser.getEmail());
                return new ServiceResult<>(true,new AuthenticationResponse(jwtToken,"User logged in."),null,0);
            }
            else {
                return new ServiceResult<>(false,null,"Wrong password",1);
            }
        }
        else {
            return new ServiceResult<>(false,null,"User not found",2);
        }
    }

    public ServiceResult<AuthenticationResponse> recoverPassword(FinancialUserDTO user) {
        //TODO: Recover password LOGIC
        return null;
    }


}
