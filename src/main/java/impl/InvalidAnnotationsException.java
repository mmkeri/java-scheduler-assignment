package impl;

/**
 * Created by mmker on 07-Mar-17.
 */
public class InvalidAnnotationsException extends RuntimeException {

    /**
     * Custom exception for use when a JAXBException is thrown while setting the JAXBContext
     * so that a more descriptive exception is passed to the calling function
     * @param innerException passes on the JAXBException's message
     */
    public InvalidAnnotationsException(Exception innerException){
        super(innerException);
    }
}
