package com.mindhub.homebanking;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest                                     //Permite usar la base de datos de H2 (no es real)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardUtilsTest {

    @Test
    public void cardFullNumberIsCreated(){
        String cardFullNumber = CardUtils.getFullNumber();
        assertThat(cardFullNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void cvvIsCreated(){
        int cvv = CardUtils.getCvv();
        assertThat(cvv, lessThanOrEqualTo(999));
    }
}