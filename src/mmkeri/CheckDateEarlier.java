package mmkeri;

import java.util.Calendar;

/**
 * Created by mmker on 05-Jan-17.
 */
public class CheckDateEarlier {

    public static boolean isDateEarlier(Calendar date) {
        /*
        Creates a Calendar that will default to the day it is created
        but start of day is set to Midnight.
        Ensures that an "earlier" date will be from the day before
        and earlier
         */

        Calendar startOfDay = Calendar.getInstance();
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 00);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);
        if (date.getTimeInMillis() < startOfDay.getTimeInMillis()) {
            return true;
        } else {
            return false;
        }
    }
}
