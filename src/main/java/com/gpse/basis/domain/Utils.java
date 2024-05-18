package com.gpse.basis.domain;

import java.util.Date;
import java.util.Random;

public class Utils {
    public Date transformString(String dateString) {
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        int day = Integer.parseInt(dateString.substring(8));
        Date date = new Date(year,month,day);
        return date;
    }
    public String generateID() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf(timestamp) + String.valueOf(randomValue);
    }
}
