package impl;

import java.util.Calendar;

/**
 * Created by mmker on 05-Jan-17.
 */
public abstract class CheckDateEarlier {

    private CheckDateEarlier() {}

    /**
     * Creates a Calendar that will default to the dat it is created
     * but start of day is set to Midnight.
     * Ensures that an "earlier" date will be from the day before
     * and earlier
     * @param date as a Calendar object
     * @return a boolean result
     */
    public static boolean isDateEarlier(Calendar date) {

        Calendar startOfDay = Calendar.getInstance();
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 00);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);
        return (date.getTimeInMillis() < startOfDay.getTimeInMillis());
    }
}
