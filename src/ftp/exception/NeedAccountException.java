package ftp.exception;

/**
 * Indicates that user need account.
 *
 * @author Vojko Vladimir
 */
public class NeedAccountException extends FTPException {

    public NeedAccountException(String msg) {
        super(msg);
    }
}
