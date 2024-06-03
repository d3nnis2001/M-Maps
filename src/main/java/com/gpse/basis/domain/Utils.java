package com.gpse.basis.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

public class Utils {
    public String generateID() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf(timestamp) + String.valueOf(randomValue);
    }
}
