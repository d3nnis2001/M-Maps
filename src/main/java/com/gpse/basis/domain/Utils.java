package com.gpse.basis.domain;

import java.util.Date;

public class Utils {
    public Date transformString(String dateString) {
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        int day = Integer.parseInt(dateString.substring(8));
        Date date = new Date(year,month,day);
        return date;
    }
}
