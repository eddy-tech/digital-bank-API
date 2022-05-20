package org.backend.banqueSI;

import org.backend.banqueSI.entities.*;
import org.backend.banqueSI.enums.AccountStatus;
import org.backend.banqueSI.enums.OperationType;
import org.backend.banqueSI.exceptions.BalanceNotSufficientException;
import org.backend.banqueSI.exceptions.BankAccountNotFoundException;
import org.backend.banqueSI.exceptions.CustomerNotFoundException;
import org.backend.banqueSI.repositories.AccountOperationRepository;
import org.backend.banqueSI.repositories.BankAccountRepository;
import org.backend.banqueSI.repositories.CustomerRepository;
import org.backend.banqueSI.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BanqueSiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanqueSiApplication.class, args);
	}
    @Bean
	CommandLineRunner start(BankAccountService bankAccountService){
		return args -> {
			Stream.of("Galbalini","César","Moukoudi","Harold","Mamadou","Mangala","BanguyTawnla")
					.forEach(name -> {
						Customer customer = new Customer();
						customer.setName(name);
						customer.setEmail(name+"@gmail.com");
						bankAccountService.saveCustomer(customer);
					});
			bankAccountService.listCustomers().forEach(customer -> {
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*12000,5.5, customer.getId());
					List<BankAccount> bankAccounts = bankAccountService.listBankAccount();
					for (BankAccount bankAccount:bankAccounts){
						for (int i=0; i<10; i++){
							bankAccountService.credit(bankAccount.getId(), 10000+Math.random()*12000,"Credit");
							bankAccountService.debit(bankAccount.getId(), 1000+Math.random()*7000,"Debit");
						}
					}
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				} catch (BankAccountNotFoundException e) {
					e.printStackTrace();
				} catch (BalanceNotSufficientException e) {
					e.printStackTrace();
				}
			});

		};
	}
	//@Bean
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
				currentAccount.setCurrency(Math.random()>0.5 ? "EUR" : "DOLLAR");
				currentAccount.setCustomer(customer);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setAccountStatus(AccountStatus.CREATED);
				currentAccount.setOverDraft(9000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()*90000);
				savingAccount.setCurrency(Math.random()>0.5 ? "EUR" : "DOLLAR");
				savingAccount.setCustomer(customer);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setAccountStatus(AccountStatus.CREATED);
				savingAccount.setInterestRate(5.5);
				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(acc ->{
				for (int i =0; i<10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*12000);
					accountOperation.setOperationType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAccount(acc);
					accountOperationRepository.save(accountOperation);
				}
			});

			BankAccount bankAccount = bankAccountRepository.findById("857b6c78-2e71-44ac-903e-af37115be03f").orElse(null);
			if(bankAccount != null) {
				System.out.println("***********************************************");
				System.out.println(bankAccount.getId());
				System.out.println(bankAccount.getBalance());
				System.out.println(bankAccount.getCurrency());
				System.out.println(bankAccount.getCreatedAt());
				System.out.println(bankAccount.getCustomer().getName());
				System.out.println(bankAccount.getClass().getSimpleName()); // AFFICHER LE TYPE DE COMPTE ET LE NOM DE CLASSE DU COMPTE
				if (bankAccount instanceof CurrentAccount) {
					System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount).getOverDraft());
				} else if (bankAccount instanceof SavingAccount) {
					System.out.println("Rate =>" + ((SavingAccount) bankAccount).getInterestRate());
				}

				// HISTORIQUE DES OPERATIONS
				bankAccount.getOperations().forEach(op -> {
					System.out.println(op.getOperationType() + "\t" + op.getOperationDate() + "\t" + op.getAmount());
				});
			}
		};
}

}
