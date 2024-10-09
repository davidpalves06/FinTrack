package org.financk.financk_backend.auth.repository;

import org.financk.financk_backend.auth.models.FinancialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FinancialUserRepository extends JpaRepository<FinancialUser, UUID> {
}
