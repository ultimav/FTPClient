package ftp.exception;

/**
 * Indicates that data connection can't be opened.
 *
 * @author Vojko Vladimir
 */
public class CantOpenDataConnectionException extends FTPException {

    public CantOpenDataConnectionException(String msg) {
        super(msg);
    }

}
