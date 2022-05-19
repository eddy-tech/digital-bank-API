package org.backend.banqueSI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.banqueSI.entities.enums.OperationType;

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
    private Date createdAt;
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
}
