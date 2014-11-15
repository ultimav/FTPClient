package ftp.exception;

/**
 * Indicates that Requested action aborted: local error in processing.
 *
 * @author Vojko Vladimir
 */
public class ActionAbortedException extends FTPException {

    public ActionAbortedException(String msg) {
        super(msg);
    }
}
