package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long Id;

    private String name;

    private double maxAmount;
    private float interes;

    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    public Loan() {}
    public Loan(String name, double maxAmount, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }
    public Loan(String name, double maxAmount, List<Integer> payments, float interes) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.interes = interes;
    }

    public long getId() {return Id;}


    public String getName() {return name;}
    public void setName(String name) {this.name = name;}


    public double getMaxAmount() {return maxAmount;}
    public void setMaxAmount(double maxAmount) {this.maxAmount = maxAmount;}


    public List<Integer> getPayments() {return payments;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}


    public float getInteres() {return interes;}
    public void setInteres(float interes) {this.interes = interes;}


    @JsonIgnore
    public Set<Client> getClient() {
        return clientLoans.stream().map(ClientLoan::getClient).collect(Collectors.toSet());
    }
}
