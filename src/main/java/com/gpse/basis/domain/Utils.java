package com.gpse.basis.domain;

import java.util.Random;

/**
 * The type Utils.
 */
public class Utils {
    /**
     * Generate id string.
     *
     * @return the string
     */
    public String generateID() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomValue = random.nextInt(1000);
        return String.valueOf(timestamp) + String.valueOf(randomValue);
    }
}
