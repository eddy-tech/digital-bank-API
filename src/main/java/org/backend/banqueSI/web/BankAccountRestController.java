package org.backend.banqueSI.web;

import lombok.AllArgsConstructor;
import org.backend.banqueSI.dtos.*;
import org.backend.banqueSI.exceptions.BalanceNotSufficientException;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;
import org.backend.banqueSI.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping(produces = "application/json",value = "/digital/bankSI/v1")
public class BankAccountRestController {
    private BankAccountService bankAccountService;


    @GetMapping(path = "/accounts/operations/{id}")
    public List<AccountOperationDTO> listAccountHistory(@PathVariable(name = "id") String accountId) {
        return bankAccountService.listAccountHistory(accountId);
    }

    @PostMapping(path = "/accounts/currentAccount")
    public CurrentBankAccountDTO saveCurrentBankAccount(
            @RequestParam double initialBalance,
            @RequestParam double overDraft,
            @RequestParam Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping(path = "/accounts/savingAccount")
    public SavingBankAccountDTO saveSavingBankAccount(
            @RequestParam double initialBalance,
            @RequestParam double interestRate,
            @RequestParam Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @GetMapping(path = "/accounts")
    public List<BankAccountDTO> listBankAccount() {
        return bankAccountService.listBankAccount();
    }

    @GetMapping(path = "/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable(name = "id") String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }


    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable(name = "id") String accountId,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @PostMapping(path = "/accounts/debit")
    public void debit(
            @RequestParam String accountId,
            @RequestParam double amount,
            @RequestParam String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(accountId, amount, description);
    }

    @PostMapping(path = "/accounts/credit")
    public void credit(
            @RequestParam String accountId,
            @RequestParam double amount,
            @RequestParam String description) throws BankAccountNotFoundException {
        bankAccountService.credit(accountId, amount, description);
    }

    @PostMapping(path = "/accounts/transfer")
    public void transfer(
            @RequestParam String accountIdSource,
            @RequestParam String accountIdDestination,
            @RequestParam double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(accountIdSource, accountIdDestination, amount);
    }


}
