package ftp.exception;

/**
 * Indicates that Requested action not taken.
 * File name not allowed.
 *
 * @author Vojko Vladimir
 */
public class FileNameNotAllowedException extends ActionNotTakenException {

    public FileNameNotAllowedException(String msg) {
        super(msg);
    }
}
