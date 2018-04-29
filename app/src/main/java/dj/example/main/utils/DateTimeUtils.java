package dj.example.main.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by DJphy on 3/4/2016.
 */
public class DateTimeUtils {

    public static long getDateInMillis(String dateFromServer) {
        int[] dateInInt = serverToUsableDate(dateFromServer);
        if (dateInInt != null) {
            return getTimeInMillisForDate(dateInInt[0], dateInInt[1], dateInInt[2]);
        }
        return 0;
    }

    public static String getDateFormatForCalendar(String date){
        String[] dateArr = date.split("-");
        return new StringBuilder(dateArr[0]).append("\r\n")
                .append(dateArr[1])/*.append("\r\n")
                .append(dateArr[2])*/.toString();
    }


    /*public static String getTime12Hour(String dateFromServer) {

        String[] arraydateTime = dateFromServer.split(" ");
        *//*Time t = Time.valueOf(arraydateTime[1].trim());
        long l = t.getTime();*//*
        return _24To12Hour(arraydateTime[1].trim());
    }*/


    public static String getFormattedTimestamp(String outputFormat, long timestamp){
        Log.d("dj", "input getFormattedTimestamp: " + outputFormat +" && "+timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat, Locale.getDefault());
        Date date = new Date(timestamp);
        Log.d("dj", "datetime: getFormattedTimestamp: " + dateFormat.format(date));
        return dateFormat.format(date);
    }


    public static String get12HourFormatTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }


    public static String getCurrentDateTime12hr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        Log.d("dj", "datetime: getCurrentDateTime12hr" + dateFormat.format(date));
        return dateFormat.format(date);
    }



    private static String _24To12Hour(String _24HrTime) {
        try {
            String _24HourTime = _24HrTime;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            System.out.println(_24HourDt);
            String time12Hr = _12HourSDF.format(_24HourDt);
            System.out.println("time in 12 hours: " + time12Hr);
            return time12Hr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static int[] serverToUsableDate(String dateFromServer) {
        try {
            String[] dateArr = dateFromServer.split("-");
            return new int[]{Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2])};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static long getTimeInMillisForDate(int year, int month, int date) {
        Log.d("dj", "year: " + year);
        Log.d("dj", "month: " + month);
        Log.d("dj", "date: " + date);
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.YEAR, year);
        tempCalendar.set(Calendar.MONTH, (month - 1));
        tempCalendar.set(Calendar.DAY_OF_MONTH, date);
        return tempCalendar.getTimeInMillis();
    }


    public static String getNewDate(String inputDate, String inputDateFormat, String outputDateFormat) {

        try {
            DateFormat dateFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());
            Date inputDateObj = dateFormat.parse(inputDate);
            String newDateFormat = new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(inputDateObj);
            Log.d("dj", "new date format string: " + newDateFormat);
            return newDateFormat;
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }

    }


    public static String getCurrentDateTime(String inputDateFormat){
        Date current = new Date();
        DateFormat newFormat = new SimpleDateFormat(inputDateFormat, Locale.getDefault());
        String currentFormatted = newFormat.format(current);
        return currentFormatted;
    }


    public static List<String> getDisplayableCalendarMenu(List<String> datelistInyyyymmdd){

        List<String> dateList = new ArrayList<>();
        for (String date: datelistInyyyymmdd){
            String temp = DateTimeUtils.getFormattedTimestamp("MMM-dd-yyyy",
                    DateTimeUtils.getDateInMillis(date));
            temp = DateTimeUtils.getDateFormatForCalendar(temp);
            dateList.add(temp);
        }
        return dateList;
    }


    public static List<String> getPreviousDateInyyyymmdd(int daysFromCurrent) {

        Calendar now = Calendar.getInstance();
        List<Date> dates = different(daysFromCurrent);
        List<String> dateList = new ArrayList<>();
        for (int i = dates.size() - 1; i>=0; i--){
            String temp = DateTimeUtils.getFormattedTimestamp("yyyy-MM-dd", dates.get(i).getTime());
            dateList.add(temp);
        }
        return dateList;
    }


    private static int getWeekLessFromCurrent(int daysFromCurrent) {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH) - daysFromCurrent;
    }

    private static Date getWeekLessFromCurrentNew(int daysFromCurrent) {
        Date date = new Date();
        Calendar now = Calendar.getInstance();
        int temp = now.get(Calendar.DAY_OF_MONTH) - daysFromCurrent;
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) - daysFromCurrent);
        return now.getTime();
    }


    private static List<Date> different(int daysFromCurrent){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        List<Date> list = new ArrayList<>();
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        for (int index = daysFromCurrent; index>=0; index--) {
            c = Calendar.getInstance();
            c.add(Calendar.DATE, -i - index);
            Date start = c.getTime();
            list.add(start);
            //daysFromCurrent--;
        }
        return list;
    }
}
