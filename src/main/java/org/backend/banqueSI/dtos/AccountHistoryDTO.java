package org.backend.banqueSI.dtos;

import lombok.Data;
import org.backend.banqueSI.entities.AccountOperation;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private String type;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
