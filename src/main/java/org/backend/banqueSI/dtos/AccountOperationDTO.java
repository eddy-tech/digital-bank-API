package org.backend.banqueSI.dtos;

import lombok.Data;
import org.backend.banqueSI.enums.OperationType;
import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private double amount;
    private Date operationDate;
    private String description;
    private OperationType operationType;
}
