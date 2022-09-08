package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping ("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping(path = "/accounts")
    public List<AccountDTO> getAccounts(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping (path = "/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @GetMapping("/clients/current/accounts")
        public List<AccountDTO> getCurrentAccount(Authentication authentication){
            Client client = clientRepository.findByEmail(authentication.getName());
            return client.getAccounts().stream().map(AccountDTO::new).collect((toList()));
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType type){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Usted ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
        }

        Account account = new Account(client, type,0, getRandomCardNumber());
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(number);

        if(account == null){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta no pertenece a este cliente", HttpStatus.FORBIDDEN);
        }

        if (account.getBalance() != 0){
            return new ResponseEntity<>("La cuenta tiene balance en ella", HttpStatus.FORBIDDEN);
        }

        transactionRepository.deleteAll(account.getTransactions());
        accountRepository.delete(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> disableAccount(Authentication authentication, @RequestParam String accountNumber){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(accountNumber);

        if (account == null){
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.BAD_REQUEST);
        }

        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("La cuenta no pertenece a este cliente", HttpStatus.BAD_REQUEST);
        }

        if (account.getBalance() != 0){
            return new ResponseEntity<>("La cuenta tiene balance en ella", HttpStatus.FORBIDDEN);
        }

        account.setActiva(false);
        accountRepository.save(account);

        return new ResponseEntity<>("La cuenta ha sido deshabilitada con éxito", HttpStatus.OK);
    }

    public String getRandomCardNumber() {
        return "VIN"+((int)((Math.random()*(99999999-10000000))+10000000));
    }
}