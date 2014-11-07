package ftp.exception;

/**
 * Indicates that the user not logged in.
 *
 * @author Vojko Vladimir
 */
public class NotLoggedInException extends FTPException {

    public NotLoggedInException(String msg) {
        super(msg);
    }
}
