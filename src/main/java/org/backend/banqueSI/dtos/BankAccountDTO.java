package org.backend.banqueSI.dtos;

import lombok.Data;
import org.backend.banqueSI.enums.AccountStatus;

import java.util.Date;

@Data
public class BankAccountDTO {
    private  double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private String type;
}
