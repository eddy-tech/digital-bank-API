package org.backend.banqueSI.dtos;

import lombok.Data;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double interestRate;
}
