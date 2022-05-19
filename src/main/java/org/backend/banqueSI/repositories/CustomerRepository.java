package org.backend.banqueSI.repositories;

import org.backend.banqueSI.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}