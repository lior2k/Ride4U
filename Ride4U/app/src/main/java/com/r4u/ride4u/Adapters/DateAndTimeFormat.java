package com.r4u.ride4u.Adapters;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeFormat {

    /**
     * Returns a combined string of the given date and time in a specific format.
     *
     * @param date The date string in the format "day month year".
     * @param time The time string in the format "hh:mm".
     * @return A string containing the combined date and time in the format "day\month\year hh:mm".
     */

    public static String getDateAndTime(String date , String time) {
        String [] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String [] Splited_String = date.split("\\s+");
        int MonthNumber =  Arrays.asList(months).indexOf(Splited_String[1])+1;
        return  Splited_String[0]+"\\"+ MonthNumber +"\\"+Splited_String[2]+" "+time;

    }

    /**
     * Compares the given date and time with the current date and time in the specified time zone.
     *
     * @param dateAndTime The date and time string in the format "dd\\MM\\yyyy HH:mm".
     * @param timeZone    The time zone to use for the comparison.
     * @return true if the given date and time is before the current date and time in the specified time zone,
     *         false otherwise.
     */

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
