package ftp;

import ftp.connection.ControlConnection;
import ftp.exception.LoginIncorrectException;
import ftp.exception.NoConnectionException;
import ftp.exception.ServiceUnavailableException;

import java.io.IOException;

/**
 * Client for FTP server.
 *
 * @author Vojko Vladimir
 */
public class FTPClient {

    /**
     * Anonymous user name and password.
     */
    public static final String ANONYMOUS = "anonymous";
    /**
     * Default port of FTP server.
     */
    public static final int DEFAULT_PORT = 21;

    private ControlConnection control;

    /**
     * Create FTP Client.
     */
    public FTPClient() {
        control = new ControlConnection();
    }

    /**
     * Establish control connection to the FTP server.
     *
     * @param host address of the host.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     */
    public void connect(String host) throws IOException, ServiceUnavailableException {
        connect(host, DEFAULT_PORT);
    }

    /**
     * Establish control connection to the FTP server.
     *
     * @param host address of the host.
     * @param port port of the server to connect.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     */
    public void connect(String host, int port) throws IOException, ServiceUnavailableException {
        control.open(host, port);
    }

    /**
     * Login on the server.
     *
     * @param user user name.
     * @param pass user password.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.LoginIncorrectException     If username or password is incorrect.
     */
    public void login(String user, String pass)
            throws IOException, NoConnectionException, ServiceUnavailableException, LoginIncorrectException {
        control.login(user, pass);
    }

}
