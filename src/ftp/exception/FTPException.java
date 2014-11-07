package ftp.exception;

/**
 * Base class for all exceptions that might happen during communication with FTP server.
 *
 * @author Vojko Vladimir
 */
public class FTPException extends Exception {

    public FTPException(String msg) {
        super(msg);
    }
}
