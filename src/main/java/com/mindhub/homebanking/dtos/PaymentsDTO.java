package com.mindhub.homebanking.dtos;

public class PaymentsDTO {

    private String accountNumber;

    private String cardNumber;

    private int cvv;

    private double amount;

    private String description;


    public PaymentsDTO() {}
    public PaymentsDTO(String accountNumber, String cardNumber, int cvv, double amount, String description) {
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getAccountNumber() {return accountNumber;}

    public String getCardNumber() {return cardNumber;}

    public int getCvv() {return cvv;}

    public double getAmount() {return amount;}

    public String getDescription() {return description;}
}
