package org.backend.banqueSI.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.banqueSI.dtos.*;
import org.backend.banqueSI.entities.*;
import org.backend.banqueSI.enums.OperationType;
import org.backend.banqueSI.exceptions.BalanceNotSufficientException;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;
import org.backend.banqueSI.mappers.BankAccountMapperImpl;
import org.backend.banqueSI.repositories.AccountOperationRepository;
import org.backend.banqueSI.repositories.BankAccountRepository;
import org.backend.banqueSI.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private BankAccountMapperImpl dtoMapper;

    //Logger log = LoggerFactory.getLogger(this.getClass().getName()); // Logger les informations qui seront liées à la classe BankAccountServiceImpl

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer= dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomerDTO = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomerDTO);
    }

    @Override
    public CustomerDTO updateCustomer (CustomerDTO customerDTO) {
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public void deleteCustomer (Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null) throw new BankAccountNotFoundException("Bank Account not found");
        Page<AccountOperation> pageOperations = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> pageOperationDTOS = pageOperations.getContent().stream().map(operations -> dtoMapper.fromAccountOperation(operations))
                .collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(pageOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountHistoryDTO.getTotalPages());
        return accountHistoryDTO;

    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) throw new CustomerNotFoundException("Customers not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount saveCurrentBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(saveCurrentBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount saveSavingBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(saveSavingBankAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                    .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<CustomerDTO> searchCustomerDTO(String keyword) {
        List<Customer> customers = customerRepository.searchCustomer(keyword);
        List<CustomerDTO> customerDTO = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTO;
    }

    @Override
    public List<AccountOperationDTO> listAccountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> accountOperationsDTOs = accountOperations.stream().map(operation -> dtoMapper.fromAccountOperation(operation))
                .collect(Collectors.toList());
        return accountOperationsDTOs;
    }

    @Override
    public List<BankAccountDTO> listBankAccount() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
            List<BankAccountDTO> bankAccountDTO = bankAccounts.stream()
                    .map(bankAccount -> {
                        if(bankAccount instanceof CurrentAccount){
                            CurrentAccount currentAccount = (CurrentAccount)bankAccount;
                            return dtoMapper.fromCurrentBankAccount(currentAccount);
                        } else {
                            SavingAccount savingAccount = (SavingAccount)bankAccount;
                            return dtoMapper.fromSavingBankAccount(savingAccount);
                        }
                    }).collect(Collectors.toList());

            return bankAccountDTO;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));
        if(bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount = (CurrentAccount)bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        } else {
            SavingAccount savingAccount = (SavingAccount)bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description)
            throws BalanceNotSufficientException, BankAccountNotFoundException {
        //BankAccount bankAccount = getBankAccount(accountId);
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("Bank Account not found"));
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
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(()-> new BankAccountNotFoundException("Bank Account not found"));
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
