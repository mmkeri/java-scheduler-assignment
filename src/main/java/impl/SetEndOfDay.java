package impl;

import java.util.Calendar;

/**
 * Created by mmker on 13-Jan-17.
 */
public abstract class SetEndOfDay {

    private SetEndOfDay(){}

    /**
     * Static method to set the end of a day at midnight of the next day based
     * on the date given then returns that value
     * @param startOfDay a Calendar object that has been previously set to midnight of the current day
     * @param endOfDay a Calendar object that was instantiated to be the current date and time
     * @return the adjusted endOfDay object to now be midnight of the next calendar day
     */
    public static Calendar setEnd(Calendar startOfDay, Calendar endOfDay){
        endOfDay.setTimeInMillis(startOfDay.getTimeInMillis());
        endOfDay.add(Calendar.DATE, 1);
        return endOfDay;
    }
}
