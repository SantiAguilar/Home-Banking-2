package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    @RequestMapping("/cards")
    public List<CardDTO> getCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if (cardRepository.findByClientAndType(client, cardType).size()>=3){
            return new ResponseEntity<>("Usted ya tiene 3 cuentas", HttpStatus.FORBIDDEN);
        }

        Card card = new Card(cardType, cardColor, client);
        client.addCard(card);
        cardRepository.save(card);
        return new ResponseEntity<>("201 creado", HttpStatus.CREATED);

    }


    @DeleteMapping("/clients/current/cards")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam String cardNumber){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        Card card = cardRepository.findByNumber(cardNumber);

        if(card == null){
            return new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
        }

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("La tarjeta no pertenece a este cliente", HttpStatus.FORBIDDEN);
        }

        cardRepository.delete(card);
        return new ResponseEntity<>("Tarjeta borrada", HttpStatus.CREATED);

    }

    //Reto nueve: Un servicio (método dentro de un controlador) TRANSACCIONES EN ESTE CASO
}
