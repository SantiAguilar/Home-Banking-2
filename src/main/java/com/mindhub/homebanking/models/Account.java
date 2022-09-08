package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.AccountRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    private AccountType type;

    private boolean activa;

    public Account() {}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;     //al ser una propiedad necesita un getter y setter

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account(String number, LocalDateTime creationDate, double balance, Client client, AccountType type) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.type = type;
        this.activa = true;
    }
    public Account(Client client, AccountType type, double balance, String number) {
        this.number = number;
        this.creationDate = LocalDateTime.now();
        this.balance = balance;
        this.client = client;
        this.type = type;
        this.activa = true;
    }

    public long getId() {
        return id;
    }


    public Set<Transaction> getTransactions() {return transactions;}
    public void setTransactions(Set<Transaction> transactions) {this.transactions = transactions;}

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }


    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }


    public double getBalance() {
        return balance;
    }
    public void setBalance( double balance) {
        this.balance = balance;
    }


    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }


    public AccountType getType() {return type;}
    public void setType(AccountType type) {this.type = type;}


    public boolean activa() {return activa;}

    public void setActiva(boolean activa) {this.activa = activa;}
}
