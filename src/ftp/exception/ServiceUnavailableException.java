package ftp.exception;

/**
 * Indicates that server is unavailable.
 *
 * @author Vojko Vladimir
 */
public class ServiceUnavailableException extends FTPException {

    public ServiceUnavailableException(String msg) {
        super(msg);
    }
}
