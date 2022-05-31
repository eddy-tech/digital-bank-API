package org.backend.banqueSI.dtos;

import lombok.Data;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO {
    private String id;
    private double overDraft;
}
