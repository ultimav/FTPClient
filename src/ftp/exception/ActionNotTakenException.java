package ftp.exception;

/**
 * Indicates that Requested action not taken.
 *
 * @author Vojko Vladimir
 */
public abstract class ActionNotTakenException extends FTPException {

    public ActionNotTakenException(String msg) {
        super(msg);
    }
}
