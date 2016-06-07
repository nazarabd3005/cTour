/**
 * @author egiadtya
 *
 */

package com.giffar.ctour.helpers;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressLint({"SimpleDateFormat", "DefaultLocale"})
public class DateHelper {
    private static final int SECOND_CONVERT = 1000;
    private static DateHelper instance;
    public static String DAY_MONTH_FORMAT = "dd MMM";
    public static String API_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String CALENDAR_DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";
    public static String EXPIRE_IN = "expire in";
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int MONTH_MILLIS = 30 * DAY_MILLIS;
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static DateHelper getInstance() {
        if (instance == null) {
            instance = new DateHelper();
        }
        return instance;
    }

    public String getMonthName(Long unixdate){
        Date date = new Date(unixdate*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        String monthName = sdf.format(date);
        return monthName;
    }

    public String getYear(Long unixdate){
        Date date = new Date(unixdate*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        String year = sdf.format(date);
        return year;
    }
    public String formatDate(String date) {
        return formatDate("yyyy-MM-dd", date, "MMMM, d yyyy");
    }

    public String formatDate(String dateFormat, String date, String toFormat) {
        return formatDate(dateFormat, date, toFormat, null, null);
    }

    public String formatDate(String dateFormat, String date, String toFormat, Locale fromLocale, Locale toLocale) {
        String formatted = "";
        DateFormat formatter = fromLocale == null ? new SimpleDateFormat(dateFormat) : new SimpleDateFormat(dateFormat, fromLocale);
        try {
            Date dateStr = formatter.parse(date);
            formatted = formatter.format(dateStr);
            Date formatDate = formatter.parse(formatted);
            formatter = toLocale == null ? new SimpleDateFormat(toFormat) : new SimpleDateFormat(toFormat, toLocale);
            formatted = formatter.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatted;
    }

    public Date getDate(String dateFormat, String date, String toFormat) {
        String formatted = "";
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Date formatDate = new Date();
        try {
            Date dateStr = formatter.parse(date);
            formatted = formatter.format(dateStr);
            formatDate = formatter.parse(formatted);
            formatter = new SimpleDateFormat(toFormat);
            formatted = formatter.format(formatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatDate;
    }

    public long  formatDateToMillis(String dateFormat, String date) {
        Date dateStr = null;
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateStr = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr != null ? dateStr.getTime() : System.currentTimeMillis();
    }
    public String formatlongtoDate(Long unixdate){
        return formatlongtoDate(unixdate,API_DATE_FORMAT);
    }

    public String formatlongtoDateApp(Long unixdate){
        return formatlongtoDate(unixdate,DATE_FORMAT);
    }
    public String formatlongtoDate(Long unixdate,String format){
        Date date = new Date(unixdate*1000); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(format); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0:00"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public String formatMillisToDate(long milliseconds) {
        return formatMillisToDate(milliseconds*1000, "yyyy-MM-dd hh:mm:ss");
    }

    public String formatMillisToDate(long milliseconds, String format) {
        Date date = new Date(milliseconds);
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public String getDateTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(new Date());
    }

    public String getDate() {
        return getDate(DATE_FORMAT);
    }

    public String getDate(String format) {
        DateFormat df = new SimpleDateFormat(format);
//		df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(new Date());
    }

    public String convertSecondToTime(int sec) {
        return String.format("%02d:%02d",
                TimeUnit.SECONDS.toHours(sec),
                TimeUnit.SECONDS.toMinutes(sec) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(sec)));
    }

    public int calculateTime(long start, long end) {
        long duration = end - start;
        float result = duration / SECOND_CONVERT;
        return Math.round(result);
    }
    public int calculateDay(long start, long end){
        long duration = end -start;
        float result =duration/DAY_MILLIS;
        return Math.round(result);
    }

    public String getDiffDate(String Date, int datediff) {
        String dateInString = Date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, datediff);
        Date resultdate = new Date(cal.getTimeInMillis());
        dateInString = sdf.format(resultdate);
        return dateInString;
    }

    public Date parseToDate(String date, String dateFormat) {
        Date _date = null;
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            _date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _date;
    }
//    public Date parseToMillis(Date date, String dateFormat) {
//        Date _date = null;
//        DateFormat formatter = new SimpleDateFormat(dateFormat);
//        try {
//            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
//            _date = formatter.parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return _date;
//    }

    public String getTimeAgo(String dateTime) {
        return getTimeAgo(dateTime, API_DATE_FORMAT);
    }

    public String getTimeAgo(String dateTime, String dateFormat) {
        long time = formatDateToMillis(dateFormat, dateTime);
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return formatDate(dateFormat, dateTime, "dd MMM yyyy");
        }else{
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "Yesterday";
            } else {
                int dayDiff = (int) (diff / DAY_MILLIS);
                if (dayDiff < 31)
                    return dayDiff + " days ago";
                else {
                    int monthDiff = dayDiff / 30;
                    if (monthDiff < 12) {
                        return monthDiff + " months ago";
                    } else {
                        return formatDate(dateFormat, dateTime, "dd MMM yyyy");
                    }
                }

            }
        }
    }

    public String getTimeLater(String dateTime) {
        return getTimeLater(dateTime, API_DATE_FORMAT);
    }

    public String getTimeLater(String dateTime, String dateFormat) {
//        long time = formatDateToMillis(dateFormat, dateTime);
        long time = Long.parseLong(dateTime);
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        if (time < now || time <= 0) {return formatDate(dateFormat, dateTime, "dd MMM yyyy");
        }else{
            final long diff = time - now;
           if (diff < 24 * HOUR_MILLIS) {
                return "expire today!";
            }else {
                int dayDiff = (int) (diff / DAY_MILLIS);
                if (dayDiff < 31)
                    return EXPIRE_IN+" "+dayDiff + " days ";
                else {
                    int monthDiff = dayDiff / 30;
                        return "expire date : "+formatDate(dateFormat, dateTime, "dd MMMM yyyy");
                }
            }
        }
    }


    public String removeTimestampHour(long date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date*1000));
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, -7);
        calendar.set(Calendar.MINUTE, 0);
        Date date1 = calendar.getTime();
        long day = date1.getTime();

        return String.valueOf(day/1000);
    }

    public long setTimeEvent(long date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date*1000));

        SimpleDateFormat sdfAmerica = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        TimeZone tzInAmerica = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(tzInAmerica);
        sdfAmerica.format(calendar.getTime());


        String date1 = sdfAmerica.format(calendar.getTime());
        long day = 0;
        try {
            day = sdfAmerica.parse(date1).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day/1000;
    }

    public Boolean hasPassed(long date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(date*1000));
        calendar.set(Calendar.AM_PM, Calendar.PM);
        calendar.set(Calendar.HOUR,4);
        calendar.set(Calendar.MINUTE, 40);
        Date date1 = calendar.getTime();
        long now = System.currentTimeMillis();
        long day = date1.getTime();
        long check = Long.valueOf("1000000000000000");
        if (day > check)
            day = day/1000;
        if (now > day) {
            return true;
        }else{
            return false;
        }
    }
    public Boolean hasPassedEvent(long date){
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(new Date(date*1000));
//        calendar.set(Calendar.AM_PM, Calendar.PM);
//        calendar.set(Calendar.HOUR,4);
//        calendar.set(Calendar.MINUTE, 40);
//        Date date1 = calendar.getTime();
        if (date < 1000000000000L){
            date = date*1000;
        }
        long now = System.currentTimeMillis();
        long check = Long.valueOf("1000000000000000");
        if (now > date) {
            return true;
        }else{
            return false;
        }
    }
    public  Boolean stillFar(long date,long endstart){
        //date
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(endstart));
        calendar.set(Calendar.DAY_OF_MONTH, -1);
        if (date > endstart) {
            return true;
        }else{
            return false;
        }
    }

    public void showDatePicker(Context context, long currentTimeMilis, DatePickerDialog.OnDateSetListener datePickerListener) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTimeMilis);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        DatePickerDialog datePicker = new DatePickerDialog(context, datePickerListener, year, month, day);
        datePicker.show();
    }

    public String getCurrentDateInSpecificFormat(Calendar currentCalDate) {
        String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
        DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM yyyy");
        return dateFormat.format(currentCalDate.getTime());
    }

    private String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }


    public int dayNow(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1){
            day = day+6;
        }else{
            day = day-1;
        }
        return day;
    }

    public long[] getDaysBetweenDates( long startdate, long enddate, int sum)
    {
        long[] dates = new long[sum];
        int i = 0;
        if (startdate == enddate){
            dates[i] = startdate;
        }else{
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date(startdate));

            while (calendar.getTime().before(new Date(enddate)))
            {
                Date result = calendar.getTime();
                dates[i] = result.getTime();
                calendar.add(Calendar.DATE, 1);
                i++;
            }
        }
        return dates;
    }
}
