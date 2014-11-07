package ftp.exception;

/**
 * Indicates that Requested action not taken.
 * File unavailable (e.g., file not found, no access).
 *
 * @author Vojko Vladimir
 */
public class ActionNotTakenException extends FTPException {

    public ActionNotTakenException(String msg) {
        super(msg);
    }
}
