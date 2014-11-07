package ftp.exception;

/**
 * Indicates that client is does not connected to the server.
 *
 * @author Vojko Vladimir
 */
public class NoConnectionException extends FTPException {

    private static final String MSG = "Server connection is not established.";

    public NoConnectionException() {
        super(MSG);
    }
}
