package org.backend.banqueSI.services;

import org.backend.banqueSI.dtos.*;
import org.backend.banqueSI.entities.BankAccount;
import org.backend.banqueSI.entities.CurrentAccount;
import org.backend.banqueSI.entities.Customer;
import org.backend.banqueSI.entities.SavingAccount;
import org.backend.banqueSI.exceptions.BalanceNotSufficientException;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    public void deleteCustomer(Long customerId);

    CustomerDTO getCustomer (Long customerId) throws CustomerNotFoundException;

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<AccountOperationDTO> listAccountHistory(String accountId);

    List<BankAccountDTO> listBankAccount();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;// CONSULTER UN COMPTE
    void debit (String accountId, double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
