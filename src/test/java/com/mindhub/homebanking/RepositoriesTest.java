package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest                                     //Permite usar la base de datos de H2 (no es real)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {


    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;



    @Test
    public void existsLoans(){
        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, is(not(empty())));
    }
    @Test
    public void existsLoansPersonal(){
        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));      //Pregunta si alguno de los Loans tiene como nombre Personal
    }
    @Test
    public void cardNumberIsCreated(){
        String getFullNumber = CardUtils.getFullNumber();

        assertThat(getFullNumber,is(not(emptyOrNullString())));
    }



    @Test
    public void existAccounts(){
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, is(not(empty())));
    }
    @Test
    public void existsAccountsVIN(){
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
    }



    @Test
    public void existClientLoans(){
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();

        assertThat(clientLoans, is(not(empty())));
    }



    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();

        assertThat(clients, is(not(empty())));
    }
    @Test
    public void clientEndsEmail(){
        List<Client> clients = clientRepository.findAll();

        assertThat("seaguilar@mindhub.com", containsString("@mindhub.com"));
    }



    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, is(not(empty())));
    }
    @Test
    public void existTransactionTypeDEBIT(){
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.DEBIT))));
    }
}