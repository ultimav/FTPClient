package ftp.exception;

/**
 * Indicates that Requested action aborted: local error in processing.
 *
 * @author Vojko Vladimir
 */
public class LocalErrorInProcessingException extends ActionAbortedException {

    public LocalErrorInProcessingException(String msg) {
        super(msg);
    }
}
