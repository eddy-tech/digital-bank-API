package org.backend.banqueSI.repositories;

import org.backend.banqueSI.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}