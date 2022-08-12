package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    private long Id;

    private String name;

    private double amount;

    private long loanId;

    private int payments;

    public ClientLoanDTO() {
    }

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.Id = clientLoan.getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.loanId = clientLoan.getLoan().getId();
        this.payments = clientLoan.getPayments();
    }

    public long getId() {return Id;}

    public String getName() {return name;}

    public double getAmount() {return amount;}

    public long getLoanId() {return loanId;}

    public int getPayments() {return payments;}
}
