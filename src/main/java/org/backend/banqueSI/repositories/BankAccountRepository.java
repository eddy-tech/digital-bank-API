package org.backend.banqueSI.repositories;

import org.backend.banqueSI.entities.AccountOperation;
import org.backend.banqueSI.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

}