package org.backend.banqueSI;

import org.backend.banqueSI.entities.CurrentAccount;
import org.backend.banqueSI.entities.Customer;
import org.backend.banqueSI.entities.SavingAccount;
import org.backend.banqueSI.entities.enums.AccountStatus;
import org.backend.banqueSI.repositories.AccountOperationRepository;
import org.backend.banqueSI.repositories.BankAccountRepository;
import org.backend.banqueSI.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BanqueSiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanqueSiApplication.class, args);
	}

	@Bean
CommandLineRunner start(CustomerRepository customerRepository,
						BankAccountRepository bankAccountRepository,
						AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Eddy", "Freddy", "Bill").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*90000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setAccountStatus(AccountStatus.CREATED);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setAccountStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});

		};
}

}
