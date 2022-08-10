package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication       //Nuestro proyecto es una aplicacionde SpringBoot
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);


	}
	@Bean   //Sin el Bean no se podria ejecutar el programa de manera correcta con todos los datos
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository){
		return(args)->   //args es solamente el nombre de una variable
		{
			Client client1 = new Client("Santiago", "Aguilar", "seaguilar@mindhub.com");
			Client client2 = new Client("Santiago", "Vieito", "santiaguilarvieito@mindhub.com");

			Account account1 = new Account( "VIN001", LocalDateTime.now(),5000);   //new llama al constructor
			Account account2 = new Account( "VIN002", LocalDateTime.now().plusDays(1),7500);
			Account account3 = new Account( "VIN001", LocalDateTime.now(),9000);
			Account account4 = new Account( "VIN002", LocalDateTime.now().plusDays(1),4500);

			client1.addAccount(account1);
			client1.addAccount(account2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000, "transferencia recibida" ,LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -4000, "Compra tienda xx" ,LocalDateTime.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1000, "transferencia recibida" ,LocalDateTime.now(), account2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200, "Compra tienda xy" ,LocalDateTime.now(), account2);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 8000, "transferencia recibida" ,LocalDateTime.now(), account3);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -2000, "Compra tienda xz" ,LocalDateTime.now(), account3);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 700, "transferencia recibida" ,LocalDateTime.now(), account4);
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, -2000, "Compra tienda xi" ,LocalDateTime.now(), account4);


			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			Loan loan1 =new Loan("Hipotecario", 500000, Arrays.asList(6, 12, 24, 36, 48, 60));
			Loan loan2 =new Loan("Personal", 100000, Arrays.asList(6, 12, 18));
			Loan loan3 =new Loan("Automotriz", 300000, Arrays.asList(6, 12, 18, 24));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
		};
	}
}
