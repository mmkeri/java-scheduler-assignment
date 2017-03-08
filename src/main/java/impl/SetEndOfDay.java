package impl;

import java.util.Calendar;

/**
 * Created by mmker on 13-Jan-17.
 */
public abstract class SetEndOfDay {

    private SetEndOfDay(){}

    /*
    Static method to set the end of a day at midnight of the next day based
    on the date given then returns that value
     */
    public static Calendar setEnd(Calendar startOfDay, Calendar endOfDay){
        endOfDay.setTimeInMillis(startOfDay.getTimeInMillis());
        endOfDay.add(Calendar.DATE, 1);
        return endOfDay;
    }
}
