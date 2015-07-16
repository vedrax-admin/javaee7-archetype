package ${package}.util;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author VEDRAX SAS
 */


public class Utils {

    private Utils() {
        // Hide constructor.
    }

    /**
     * Returns <code>true</code> if the given string is null or is empty.
     *
     * @param string The string to be checked on emptiness.
     * @return <code>true</code> if the given string is null or is empty.
     */
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Returns <code>true</code> if the given array is null or is empty.
     *
     * @param array The array to be checked on emptiness.
     * @return <code>true</code> if the given array is null or is empty.
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Returns <code>true</code> if the given collection is null or is empty.
     *
     * @param collection The collection to be checked on emptiness.
     * @return <code>true</code> if the given collection is null or is empty.
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Returns <code>true</code> if the given map is null or is empty.
     *
     * @param map The map to be checked on emptiness.
     * @return <code>true</code> if the given map is null or is empty.
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Returns <code>true</code> if the given value is null or is empty. Types
     * of String, Collection, Map and Array are recognized. If none is
     * recognized, then examine the emptiness of the toString() representation
     * instead.
     *
     * @param value The value to be checked on emptiness.
     * @return <code>true</code> if the given value is null or is empty.
     */
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof String) {
            return ((String) value).isEmpty();
        } else if (value instanceof Collection<?>) {
            return ((Collection<?>) value).isEmpty();
        } else if (value instanceof Map<?, ?>) {
            return ((Map<?, ?>) value).isEmpty();
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        } else {
            return value.toString() == null || value.toString().isEmpty();
        }
    }

    /**
     * Returns <code>true</code> if at least one value is empty.
     *
     * @param values the values to be checked on emptiness
     * @return <code>true</code> if any value is empty and <code>false</code> if
     * no values are empty
     * @since 1.8
     */
    public static boolean isAnyEmpty(Object... values) {
        for (Object value : values) {
            if (isEmpty(value)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns <code>true</code> if the given string is null or is empty or
     * contains whitespace only. In addition to {@link #isEmpty(String)}, this
     * thus also returns <code>true</code> when
     * <code>string.trim().isEmpty()</code> returns <code>true</code>.
     *
     * @param string The string to be checked on blankness.
     * @return True if the given string is null or is empty or contains
     * whitespace only.
     * @since 1.5
     */
    public static boolean isBlank(String string) {
        return isEmpty(string) || string.trim().isEmpty();
    }

    /**
     * Returns <code>true</code> if the given string is parseable as a number.
     * I.e. it is not null, nor blank and contains solely digits. I.e., it won't
     * throw a <code>NumberFormatException</code> when parsing as
     * <code>Long</code>.
     *
     * @param string The string to be checked as number.
     * @return <code>true</code> if the given string is parseable as a number.
     * @since 1.5.
     */
    public static boolean isNumber(String string) {
        try {
            // Performance tests taught that this approach is in general faster than regex or char-by-char checking.
            Long.parseLong(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns <code>true</code> if the given string is parseable as a decimal.
     * I.e. it is not null, nor blank and contains solely digits. I.e., it won't
     * throw a <code>NumberFormatException</code> when parsing as
     * <code>Double</code>.
     *
     * @param string The string to be checked as decimal.
     * @return <code>true</code> if the given string is parseable as a decimal.
     * @since 1.5.
     */
    public static boolean isDecimal(String string) {
        try {
            // Performance tests taught that this approach is in general faster than regex or char-by-char checking.
            Double.parseDouble(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Remove Object from Collection 1
     *
     * @param list
     * @param remove
     */
    public static void removeFromListIfAny(Collection<?> list, Collection<?> remove) {
        if (!isAnyEmpty(list, remove)) {
            Iterator<?> iterator = list.iterator();

            while (iterator.hasNext()) {
                if (remove.contains(iterator.next())) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Add parameter to Date
     *
     * @param date
     * @param calendarField
     * @param amount
     * @return Date
     */
    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * Add year to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    /**
     * Add Months to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    /**
     * Add Weeks to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addWeeks(final Date date, final int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    /**
     * Add Days to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    /**
     * Add Hours to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    /**
     * Add Minutes to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    /**
     * Add Seconds to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    /**
     * Add Milliseconds to Date
     *
     * @param date
     * @param amount
     * @return Date
     */
    public static Date addMilliseconds(final Date date, final int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

}
