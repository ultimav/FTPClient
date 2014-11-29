package ftp.exception;

/**
 * Indicates that Requested action aborted: page type unknown.
 *
 * @author Vojko Vladimir
 */
public class PageTypeUnknownException extends ActionAbortedException {

    public PageTypeUnknownException(String msg) {
        super(msg);
    }
}
