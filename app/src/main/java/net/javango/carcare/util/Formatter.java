package net.javango.carcare.util;

/**
 * Utility for miscellaneous formatting
 */
public class Formatter {

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
     * Converts a number to string. If input is null, returns emtpy string
     */
    public static String toString(Integer number) {
        if (number == null)
            return "";
        return String.valueOf(number);
    }

}
