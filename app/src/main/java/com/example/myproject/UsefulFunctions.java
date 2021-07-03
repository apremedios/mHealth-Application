package com.example.myproject;

import java.util.Calendar;

public class UsefulFunctions {

    private static Calendar calendar;


    //get current date and time
    public static String getCurrentDate(){

        calendar =  Calendar.getInstance();
        String currentDate = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        return currentDate;
    }

    public static String getCurrentTime(){

        calendar =  Calendar.getInstance();
        String currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
        return currentTime;
    }

    private static String pad(int i) {
        if(i<10){
            return "0" + i;
        }
        return String.valueOf(i);
    }
}
