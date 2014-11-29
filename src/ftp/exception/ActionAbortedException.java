package ftp.exception;

/**
 * Indicates that Requested action aborted.
 *
 * @author Vojko Vladimir
 */
public abstract class ActionAbortedException extends FTPException {

    public ActionAbortedException(String msg) {
        super(msg);
    }
}
