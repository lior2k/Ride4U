package com.r4u.ride4u.Adapters;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeFormat {

    public static String getDateAndTime(String date , String time) {
        String [] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String [] Splited_String = date.split("\\s+");
        int MonthNumber =  Arrays.asList(months).indexOf(Splited_String[1])+1;
        return  Splited_String[0]+"\\"+ MonthNumber +"\\"+Splited_String[2]+" "+time;

    }

    public static boolean compareDateAndTime(String dateAndTime, String timeZone) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd\\MM\\yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date PostDate;
        try {
            PostDate = format.parse(dateAndTime);
            return (new Date().after(PostDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
