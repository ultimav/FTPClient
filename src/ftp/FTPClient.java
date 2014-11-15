package ftp;

import ftp.connection.*;
import ftp.exception.*;

import java.io.IOException;
import java.util.ArrayList;

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
    private DataConnection data;

    /**
     * Create FTP Client.
     */
    public FTPClient() {
        control = new ControlConnection();
        data = new DataConnection();
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

    /**
     * Open data connection in the passive mode.
     *
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     */
    private void openPassiveDTP()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException {
        Replay replay = control.sendCommand(Command.PASSIVE);

        switch (replay.code) {
            case ReplayCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(replay.text);
            case ReplayCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(replay.text);
        }

        int startIndex = replay.text.indexOf('(') + 1;
        int stopIndex = replay.text.indexOf(')');
        String hostPort = replay.text.substring(startIndex, stopIndex);
        String[] address = hostPort.split(",");

        String host = address[0] + "." + address[1] + "." + address[2] + "." + address[3];
        int port = Integer.parseInt(address[4]) * 256 + Integer.parseInt(address[5]);

        data.open(host, port);
    }

    /**
     * Get list of files of current working directory from the server.
     *
     * @return list of files.
     * @throws java.io.IOException                           If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException           If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException     If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException            If user not logged in.
     * @throws ftp.exception.FileActionNotTakenException     If file unavailable (e.g., file busy).
     * @throws ftp.exception.CantOpenDataConnectionException If data connection can't be opened.
     * @throws ftp.exception.ConnectionClosedException       If connection closed.
     * @throws ftp.exception.ActionAbortedException          If action aborted.
     */
    public ArrayList<RemoteFile> getFilesList()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            FileActionNotTakenException, CantOpenDataConnectionException, ConnectionClosedException,
            ActionAbortedException {
        return getFilesList("");
    }

    /**
     * Get list of files in the specified directory (pathName).
     *
     * @param pathName directory path.
     * @return list of files.
     * @throws java.io.IOException                           If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException           If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException     If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException            If user not logged in.
     * @throws ftp.exception.FileActionNotTakenException     If file unavailable (e.g., file busy).
     * @throws ftp.exception.CantOpenDataConnectionException If data connection can't be opened.
     * @throws ftp.exception.ConnectionClosedException       If connection closed.
     * @throws ftp.exception.ActionAbortedException          If action aborted.
     */
    public ArrayList<RemoteFile> getFilesList(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            FileActionNotTakenException, CantOpenDataConnectionException, ConnectionClosedException,
            ActionAbortedException {
        openPassiveDTP();

        Replay replay = control.sendCommand(Command.LIST + pathName);

        switch (replay.code) {
            case ReplayCode.REQUESTED_FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(replay.text);
            case ReplayCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(replay.text);
            case ReplayCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(replay.text);
            case ReplayCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(replay.text);
            case ReplayCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(replay.text);
            case ReplayCode.REQUESTED_ACTION_ABORTED:
                throw new ActionAbortedException(replay.text);
        }

        ArrayList<RemoteFile> filesList = new ArrayList<RemoteFile>();

        for (String fileProperty : data.readLines()) {
            filesList.add(new RemoteFile(fileProperty));
        }

        replay = control.readReplay();

        data.close();

        switch (replay.code) {
            case ReplayCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(replay.text);
            case ReplayCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(replay.text);
            case ReplayCode.REQUESTED_ACTION_ABORTED:
                throw new ActionAbortedException(replay.text);
        }

        return filesList;
    }

}
