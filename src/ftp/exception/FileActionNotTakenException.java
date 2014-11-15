package ftp.exception;

/**
 * Indicates that Requested file action not taken.
 * File unavailable (e.g., file busy).
 *
 * @author Vojko Vladimir
 */
public class FileActionNotTakenException extends FTPException {

    public FileActionNotTakenException(String msg) {
        super(msg);
    }
}
