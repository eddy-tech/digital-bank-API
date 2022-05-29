package org.backend.banqueSI.services;

import org.backend.banqueSI.dtos.CustomerDTO;
import org.backend.banqueSI.entities.BankAccount;
import org.backend.banqueSI.entities.CurrentAccount;
import org.backend.banqueSI.entities.Customer;
import org.backend.banqueSI.entities.SavingAccount;
import org.backend.banqueSI.exceptions.BalanceNotSufficientException;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer (Customer customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<BankAccount> listBankAccount();
    CustomerDTO getCustomer(Long customerId);
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;// CONSULTER UN COMPTE
    void debit (String accountId, double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
