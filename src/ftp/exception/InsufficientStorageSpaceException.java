package ftp.exception;

/**
 * Indicates that Requested action not taken.
 * Insufficient storage space in system.
 *
 * @author Vojko Vladimir
 */
public class InsufficientStorageSpaceException extends ActionNotTakenException {

    public InsufficientStorageSpaceException(String msg) {
        super(msg);
    }
}
