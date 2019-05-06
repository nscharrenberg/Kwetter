package com.nscharrenberg.kwetter.selenium.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SeleniumTestDirectoryCreator {
    public static String createTestFolder(String name) {
        Date dateTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String stringDate = dateFormat.format(dateTime);

        return name + "_" + stringDate;
    }
}
