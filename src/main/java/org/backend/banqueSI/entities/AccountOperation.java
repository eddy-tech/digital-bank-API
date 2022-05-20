package org.backend.banqueSI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.banqueSI.enums.OperationType;

import javax.persistence.*;
import java.util.Date;
@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
@Table(name = "Operations")
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    @Temporal(TemporalType.DATE)
    private Date operationDate;
    private String description;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
}
