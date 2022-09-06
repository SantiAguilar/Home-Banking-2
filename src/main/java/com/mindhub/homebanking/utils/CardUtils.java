package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.repositories.CardRepository;

public final class CardUtils {

    public CardUtils() {}

    public static String getFullNumber(){
        int val1 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int val2 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int val3 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        int val4 = (int) ((Math.random() * (9999 - 1000)) + 1000);
        return val1 + "-" + val2 + "-" + val3 + "-" + val4;
    }

    public static int getCvv() {
        return (int) ((Math.random() * (999 - 100)) + 100);
    }

}
