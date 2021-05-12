package pl.codesafe.exception;

/**
 * @author LiYan
 */
public class UnHandledException extends RuntimeException {

    public UnHandledException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
