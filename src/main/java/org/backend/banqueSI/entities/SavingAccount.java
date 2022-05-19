package org.backend.banqueSI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("CE")
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class SavingAccount extends BankAccount{
    private double interestRate;
}
