package impl;

/**
 * Created by mmker on 07-Mar-17.
 */

/**
 * A JaxBException should be a rare occurrence. But this Exception is more descriptive
 * of what fault has occurred in this particular setting. Forwards the message from the
 * original exception.
 */
public class ContactManagerDecodeException extends RuntimeException {
    public ContactManagerDecodeException(Exception innerException) {
        super(innerException);
    }
}
