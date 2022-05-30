package org.backend.banqueSI.dtos;

import lombok.Data;
import org.backend.banqueSI.entities.Customer;
import org.backend.banqueSI.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private  double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
