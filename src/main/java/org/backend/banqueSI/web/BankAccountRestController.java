package org.backend.banqueSI.web;

import lombok.AllArgsConstructor;
import org.backend.banqueSI.dtos.*;
import org.backend.banqueSI.entities.BankAccount;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;
import org.backend.banqueSI.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
public class BankAccountRestController {
    private BankAccountService bankAccountService;


    @PostMapping(path = "/accounts/currentAccount")
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping(path = "/accounts/savingAccount")
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
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

    public List<AccountOperationDTO> getHistory (String accountId) {
            return bankAccountService.getAccountHistory(accountId);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable(name = "id") String accountId,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

}
