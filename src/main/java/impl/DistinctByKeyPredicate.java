package impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;

/**
 * Created by mmker on 06-Jan-17.
 */
public class DistinctByKeyPredicate {

    /**
     * Predicate created to help sort the list of Meetings by the date
     * @param keyExtractor a special purpose ValueExtractor implementation that indicates that a query
     *                     should be run against the key of a map rather than the values
     * @param <T> sorting function that accepts a generic map object
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor){
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
