package one.servises.managers.dateTimeManager;

import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class DateTimeManager {

    /**
     *  Transforms {@link Date} to {@link java.sql.Date}
     */
    public java.sql.Date fromUtilDateToSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     *  Transforms {@link String} to {@link java.sql.Date} in format(yyyy-MM-dd)
     */
    public java.sql.Date fromStringToSqlDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date d = null;
        try {
            d = new java.sql.Date(df.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     *  Transforms {@link Date} to {@link String} in format(dd-MM-YYYY)
     */
    public String fromDateToString(Date date) {
        DateFormat df = new SimpleDateFormat("dd-MM-YYYY");
        return df.format(date);
    }

    /**
     *  Transforms {@link String} to {@link Time} in format(hh:mm)
     */
    public Time fromStringToTime(String time) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Time t = null;
        try {
            t = new Time(df.parse(time).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     *  Transforms {@link Time} to {@link String} in format(HH:mm)
     */
    public String fromTimeToString(Time time) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(time);
    }
}
