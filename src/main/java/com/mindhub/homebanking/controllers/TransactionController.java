package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transactionMake(Authentication authentication,
              @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,

              @RequestParam double amount, @RequestParam String description) {

        Client client = clientRepository.findByEmail(authentication.getName());
        Account fromAccount = accountRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByNumber(toAccountNumber);

        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || description.isEmpty() || amount < 1) {
            return new ResponseEntity<>("Los parametros estan vacios", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Los numeros de cuenta son iguales", HttpStatus.FORBIDDEN);
        }

        if (fromAccount == null) {                            //Se utiliza el accountRepository porque las cuentas se encuentran en account
            return new ResponseEntity<>("La cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }

        if (toAccount == null) {                              //Se utiliza el accountRepository porque las cuentas se encuentran en account
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }

        if (!client.getAccounts().contains(fromAccount)) {
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }

        if (fromAccount.getBalance() < amount) {              //accountRepository.findByNumber(fromAccountNumber) obtengo algo que quiero de la cuenta
            return new ResponseEntity<>("La cuenta de destino no cuenta con el dinero necesario", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(TransactionType.DEBIT, -amount, description, accountRepository.findByNumber(fromAccountNumber));
        Transaction toTransaction = new Transaction(TransactionType.CREDIT, +amount, description, accountRepository.findByNumber(toAccountNumber));
        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);


        return new ResponseEntity<>("201 creado", HttpStatus.CREATED);
    }

    @GetMapping("/transactions/gets")
    public ResponseEntity<Object> transactionMake(Authentication authentication, @RequestParam String fromDate, @RequestParam String thruDate, @RequestParam String number){

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(number);

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate thru = LocalDate.parse(thruDate);
        LocalTime localTime = LocalTime.MIDNIGHT;
        LocalDateTime fromDate2 = LocalDateTime.of(from, localTime);
        LocalDateTime thruDate2 = LocalDateTime.of(thru, localTime);

        Set<TransactionDTO> transacciones = transactionRepository.findByDateBetween(fromDate2, thruDate2).stream().filter(transaction -> transaction.getAccount().equals(account)).map(TransactionDTO::new).collect(Collectors.toSet());

        return new ResponseEntity<>("201 creado", HttpStatus.CREATED);
    }
}
