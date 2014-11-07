package ftp;

import ftp.connection.Command;
import ftp.connection.ControlConnection;
import ftp.connection.Replay;
import ftp.connection.ReplayCode;
import ftp.exception.ActionNotTakenException;
import ftp.exception.NotLoggedInException;
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
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     */
    public void login(String user, String pass)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException {
        control.login(user, pass);
    }

    /**
     * Get the name of the current working directory.
     *
     * @return path with name of the current working directory.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     */
    public String printWorkingDirectory()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException {
        Replay replay = control.sendCommand(Command.PRINT_WORKING_DIRECTORY);
        switch (replay.code) {
            case ReplayCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(replay.text);
            case ReplayCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(replay.text);
        }
        return replay.text;
    }

    /**
     * Change working directory.
     *
     * @param pathName path with name of directory to change
     *                 (only name if new working directory is in the current directory).
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.ActionNotTakenException     If action not taken.
     */
    public void changeWorkingDirectory(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException {
        Replay replay = control.sendCommand(Command.CHANGE_WORKING_DIRECTORY + pathName);
        switch (replay.code) {
            case ReplayCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(replay.text);
            case ReplayCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(replay.text);
            case ReplayCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(replay.text);
        }
    }

    /**
     * Change working directory to the parent directory.
     *
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.ActionNotTakenException     If action not taken.
     */
    public void changeToParentDirectory()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException {
        Replay replay = control.sendCommand(Command.CHANGE_TO_PARENT_DIRECTORY);
        switch (replay.code) {
            case ReplayCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(replay.text);
            case ReplayCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(replay.text);
            case ReplayCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(replay.text);
        }
    }

    /**
     * Make directory in the specified path and name.
     *
     * @param pathName path with name of directory to make
     *                 (only the name of the new directory that must be created in the current directory).
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.ActionNotTakenException     If action not taken.
     */
    public void makeDirectory(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException {
        Replay replay = control.sendCommand(Command.MAKE_DIRECTORY + pathName);
        switch (replay.code) {
            case ReplayCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(replay.text);
            case ReplayCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(replay.text);
            case ReplayCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(replay.text);
        }
    }
}
