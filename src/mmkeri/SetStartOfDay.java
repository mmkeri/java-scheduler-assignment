package mmkeri;

import java.util.Calendar;

/**
 * Created by mmker on 13-Jan-17.
 */
public class SetStartOfDay {

    /*
    Sets the start of the day as midnight of the day given
     then returns that as a Calendar value
     */

    public static Calendar setStart(Calendar startOfDay){
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 00);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);
        return startOfDay;
    }
}
