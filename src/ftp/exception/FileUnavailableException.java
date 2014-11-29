package ftp.exception;

/**
 * Requested action not taken.
 * File unavailable (e.g., file not found, no access).
 *
 * @author Vojko Vladimir
 */
public class FileUnavailableException extends ActionNotTakenException {

    public FileUnavailableException(String msg) {
        super(msg);
    }
}
