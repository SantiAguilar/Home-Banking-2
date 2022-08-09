package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource                                                 //Anotacion para poder usar los recursos de Rest(va a trabajar con Rest)
public interface ClientRepository extends JpaRepository<Client, Long>{  //va a heredar los metodos de Jpa repository   //JPA va a trabajar con Account
// El extends hace que herede todos los metodos entre si

}
