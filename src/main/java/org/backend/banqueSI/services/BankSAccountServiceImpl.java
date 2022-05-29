package org.backend.banqueSI.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.backend.banqueSI.dtos.CustomerDTO;
import org.backend.banqueSI.entities.*;
import org.backend.banqueSI.enums.OperationType;
import org.backend.banqueSI.exceptions.BalanceNotSufficientException;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;
import org.backend.banqueSI.mappers.BankAccountMapperImpl;
import org.backend.banqueSI.repositories.AccountOperationRepository;
import org.backend.banqueSI.repositories.BankAccountRepository;
import org.backend.banqueSI.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankSAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;


    //Logger log = LoggerFactory.getLogger(this.getClass().getName()); // Logger les informations qui seront liées à la classe BankAccountServiceImpl

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new Customer");
        Customer saveCustomer = customerRepository.save(customer);
        return saveCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) throw new CustomerNotFoundException("Customers not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount saveCurrentBankAccount = bankAccountRepository.save(currentAccount);
        return saveCurrentBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saveSavingBankAccount = bankAccountRepository.save(savingAccount);
        return saveSavingBankAccount;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        //PROGRAMMATION FONCTIONNELLE
        List<CustomerDTO> customerDTOS= customers.stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer))
                   .collect(Collectors.toList());
        /*
                //PROGRAMMATION IMPERATIVE
         */
        /*
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        */
        return customerDTOS;
    }

    @Override
    public List<BankAccount> listBankAccount() { return bankAccountRepository.findAll(); }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        
        return null;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description)
            throws BalanceNotSufficientException, BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        if(bankAccount.getBalance()<amount) throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation operation = new AccountOperation();
        operation.setBankAccount(bankAccount);
        operation.setOperationType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        accountOperationRepository.save(operation);
        //UPDATE BALANCE
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        AccountOperation operation = new AccountOperation();
        operation.setOperationType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        accountOperationRepository.save(operation);
        //UPDATE BALANCE
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to"+ accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from"+accountIdSource);

    }
}
