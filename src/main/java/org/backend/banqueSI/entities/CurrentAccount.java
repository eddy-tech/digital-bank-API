package org.backend.banqueSI.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@DiscriminatorValue("CC")
@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class CurrentAccount extends BankAccount{
    private double overDraft; // decouvert
}
