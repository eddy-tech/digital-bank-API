package org.backend.banqueSI.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.backend.banqueSI.entities.Customer;
import org.backend.banqueSI.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping(path = "/customers")
    public List<Customer> listCustomers() {
        return bankAccountService.listCustomers();
    }
}
