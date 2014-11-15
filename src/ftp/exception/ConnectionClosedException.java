package ftp.exception;

/**
 * Indicates that: connection closed; transfer aborted.
 *
 * @author Vojko Vladimir
 */
public class ConnectionClosedException extends FTPException {

    public ConnectionClosedException(String msg) {
        super(msg);
    }

}
