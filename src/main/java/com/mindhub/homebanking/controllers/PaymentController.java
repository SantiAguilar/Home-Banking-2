package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentsDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
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

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    @PostMapping(path = "/payments")
    public ResponseEntity<Object> transactionMake(Authentication authentication, @RequestBody PaymentsDTO paymentsDTO){

        String accountNumber = paymentsDTO.getAccountNumber();
        String cardNumber = paymentsDTO.getCardNumber();
        int cvv = paymentsDTO.getCvv();
        double amount = paymentsDTO.getAmount();
        String description = paymentsDTO.getDescription();

        Account account = accountRepository.findByNumber(accountNumber);
        Card card = cardRepository.findByNumber(cardNumber);
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (accountNumber.isEmpty() || cardNumber.isEmpty() || description.isEmpty() || amount < 1) {
            return new ResponseEntity<>("Los parametros estan vacios", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        if(card == null){
            return new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
        }

        if(account.getBalance() < amount){
            return new ResponseEntity<>("La cuenta de destino no cuenta con el dinero necesario", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("La tarjeta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }

        if(card.getCvv() != cvv){
            return new ResponseEntity<>("El numero de seguridad es erróneo", HttpStatus.FORBIDDEN);
        }

        if(card.getThruDate().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("La tarjeta ya se encuentra vencida", HttpStatus.FORBIDDEN);
        }

        Transaction fromTransaction = new Transaction(TransactionType.DEBIT, -amount, description, accountRepository.findByNumber(accountNumber));
        transactionRepository.save(fromTransaction);

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        return new ResponseEntity<>("201 creado", HttpStatus.CREATED);
    }
}
