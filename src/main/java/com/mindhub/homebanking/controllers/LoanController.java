package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @GetMapping("/loans")
    public List<LoanDTO> getAllLoan(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());     //obtiene todos los loans
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyForLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        double amount = loanApplicationDTO.getAmount();
        int payments = loanApplicationDTO.getPayments();
        String toAccountNumber = loanApplicationDTO.gettoAccountNumber();

        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId());
        Account toAccount = accountRepository.findByNumber(toAccountNumber);


        if(amount < 1 || payments < 1){
            return new ResponseEntity<>("Los parametros estan vacios", HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("El prestamo no existe", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount() < amount){
            return new ResponseEntity<>("El monto requerido es mayor al del prestamo seleccionado", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(payments)){
            return new ResponseEntity<>("La cantidad de cuotas no es disponible para este prestamo", HttpStatus.FORBIDDEN);
        }

        if(toAccount == null){
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(toAccount)){
            return new ResponseEntity<>("La cuenta de destino no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientloanrequest = new ClientLoan(amount + amount* loan.getInteres(), payments, client, loan);
        clientLoanRepository.save(clientloanrequest);

        Transaction toTransaction = new Transaction(TransactionType.CREDIT, +amount, "prestamo aprobado", toAccount);
        transactionRepository.save(toTransaction);

        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);

        return new ResponseEntity<>("201 creado", HttpStatus.CREATED);
    }
}
