package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long Id;

    private String cardHolder;

    private String number;

    private int cvv;

    private LocalDateTime fromDate;

    private LocalDateTime thruDate;

    private CardType type;

    private CardColor color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;


    public Card() {
    }

    public Card(Client client, String cardHolder, CardType type, CardColor color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.type = type;
        this.color = color;
        this.client = client;
    }

    public Card(CardType type, CardColor color, Client client) {
        this.number = getFullNumber();
        this.type = type;
        this.color = color;
        this.client = client;
        this.cvv = ((int)((Math.random()*(999-100))+100));
        this.fromDate = LocalDateTime.now();
        this.thruDate = LocalDateTime.now().plusYears(5);
    }

    public String getFullNumber(){
        int val1 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int val2 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int val3 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int val4 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        return val1 + "-" + val2 + "-" + val3 + "-" + val4;
    }
    public long getId() {return Id;}


    public String getCardHolder() {return cardHolder;}
    public void setCardHolder(String cardHolder) {this.cardHolder = cardHolder;}

    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}


    public int getCvv() {return cvv;}
    public void setCvv(int cvv) {this.cvv = cvv;}


    public LocalDateTime  getFromDate() {return fromDate;}
    public void setFromDate(LocalDateTime  fromDate) {this.fromDate = fromDate;}


    public LocalDateTime  getThruDate() {return thruDate;}
    public void setThruDate(LocalDateTime  thruDate) {this.thruDate = thruDate;}


    public CardType getType() {return type;}
    public void setType(CardType type) {this.type = type;}


    public CardColor getColor() {return color;}
    public void setColor(CardColor color) {this.color = color;}


    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
}
