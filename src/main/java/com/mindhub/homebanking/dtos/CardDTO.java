package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;

public class CardDTO {

    private long Id;

    private String cardHolder;

    private String number;

    private int cvv;

    private LocalDateTime fromDate;

    private LocalDateTime thruDate;

    private CardType type;

    private CardColor color;


    public CardDTO() {
    }

    public CardDTO(Card card) {
        Id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.type = card.getType();
        this.color = card.getColor();
    }


    public long getId() {return Id;}

    public String getCardHolder() {return cardHolder;}

    public String getNumber() {return number;}

    public int getCvv() {return cvv;}

    public LocalDateTime getFromDate() {return fromDate;}

    public LocalDateTime getThruDate() {return thruDate;}

    public CardType getType() {return type;}

    public CardColor getColor() {return color;}
}
