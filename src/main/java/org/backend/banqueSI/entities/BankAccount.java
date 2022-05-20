package org.backend.banqueSI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.banqueSI.enums.AccountStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "bank_account")
@Data @AllArgsConstructor @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4)
public abstract class BankAccount {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    private  double balance; // solde
    private String currency; // device
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private Collection<AccountOperation> operations = new ArrayList<>();






}