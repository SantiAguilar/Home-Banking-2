package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping ("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return this.accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping ("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return this.accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.GET)
        public List<AccountDTO> getCurrentAccount(Authentication authentication){
            Client client = clientRepository.findByEmail(authentication.getName());
            return client.getAccounts().stream().map(AccountDTO::new).collect((toList()));
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Usted ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
        }
        Account account = new Account( client, getRandomCardNumber(), 0);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    public String getRandomCardNumber() {
        return "VIN"+((int)((Math.random()*(99999999-10000000))+10000000));
    }
}