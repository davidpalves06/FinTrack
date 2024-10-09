package org.financk.financk_backend.auth.api;

import org.financk.financk_backend.auth.models.FinancialUserDTO;

public class AuthenticationRequestValidator {
    public static boolean validateRegisterDTO(FinancialUserDTO userDTO) {
        return userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
                && userDTO.getName() != null && !userDTO.getName().isEmpty()
                && userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()
                && userDTO.getAge() > 0;
    }

    public static boolean validateLoginDTO(FinancialUserDTO userDTO) {
        return userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()
                && userDTO.getEmail() != null && !userDTO.getEmail().isEmpty();
    }
}
