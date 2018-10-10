package net.javango.carcare.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for miscellaneous formatting
 */
public class Formatter {

    private static final String SHORT_DATE_FORMAT = "M/d/yy";
    private static final String DATE_FORMAT = "EEE, MMM d, yyyy";

    private Formatter() {
        // non-instantiable
    }

    /**
     * Converts a string to integer. If input is null or empty or is not a valid integer, returns null
     */
    public static Integer parseInt(String s) {
        if (s == null || s.length() == 0)
            return null;

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converts a number to string. If input is null, returns empty string
     */
    public static String format(Integer number) {
        if (number == null)
            return "";
        return String.valueOf(number);
    }

    /**
     * Converts a string to a date. If input is null or empty, or is not a valid date , returns null
     */
    public static Date parseDate(String s) {
        if (s == null || s.length() == 0)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dateFormat.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converts a date to string based on provided format. If input is null, returns empty string
     */
    private static String format(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String format(Date date) {
        return format(date, SHORT_DATE_FORMAT);
    }

    public static String formatFull(Date date) {
        return format(date, DATE_FORMAT);
    }

}
