package impl;

/**
 * Created by mmker on 07-Mar-17.
 */
public class InvalidAnnotationsException extends RuntimeException {
    public InvalidAnnotationsException(Exception innerException){
        super(innerException);
    }
}
