package com.r4u.ride4u.Adapters;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeFormat {

    public static String getDateAndTime(String Date , String Time) {
        String [] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String [] Splited_String = Date.split("\\s+");
        int MonthNumber =  Arrays.asList(months).indexOf(Splited_String[1])+1;
        return  Splited_String[0]+"\\"+ MonthNumber +"\\"+Splited_String[2]+" "+Time;

    }

    public static boolean CompareDateAndTime(String DateAndTime, String timeZone) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd\\MM\\yyyy HH:mm");
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date PostDate;
        try {
            PostDate = format.parse(DateAndTime);
            return (new Date().after(PostDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
