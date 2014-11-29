package ftp.exception;

/**
 * Indicates that Requested file action aborted.
 * Exceeded storage allocation (for current directory or dataset).
 *
 * @author Vojko Vladimir
 */
public class FileActionAbortedException extends FTPException {

    public FileActionAbortedException(String msg) {
        super(msg);
    }
}
