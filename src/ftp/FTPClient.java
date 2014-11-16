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
        Reply reply = control.sendCommand(Command.PRINT_WORKING_DIRECTORY);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
        }
        return reply.text;
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
        Reply reply = control.sendCommand(Command.CHANGE_WORKING_DIRECTORY + pathName);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
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
        Reply reply = control.sendCommand(Command.CHANGE_TO_PARENT_DIRECTORY);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
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
        Reply reply = control.sendCommand(Command.MAKE_DIRECTORY + pathName);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
        }
    }

    /**
     * This command causes the directory specified in the pathname to be removed as a directory
     * (if the pathname is absolute) or as a subdirectory of the current working directory
     * (if the pathname is relative).
     *
     * @param pathName path with name of directory to remove.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.ActionNotTakenException     If action not taken.
     */
    public void removeDirectory(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException {
        Reply reply = control.sendCommand(Command.REMOVE_DIRECTORY + pathName);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
        }
    }

    /**
     * Rename file.
     *
     * @param fromPathName old file name (with full path, or without if file is in current working directory).
     * @param toPathName   new file name (with full path, or without if file is in current working directory).
     * @throws java.io.IOException                               If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException               If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException         If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException                If user not logged in.
     * @throws ftp.exception.ActionNotTakenException             If action not taken.
     * @throws ftp.exception.FileActionNotTakenException         If file unavailable (e.g., file busy).
     * @throws ftp.exception.FileNameNotAllowedException         If file nam is not allowed.
     * @throws ftp.exception.NeedAccountForStoringFilesException If user need account for storing files.
     */
    public void renameFromTo(String fromPathName, String toPathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException, FileActionNotTakenException, FileNameNotAllowedException,
            NeedAccountForStoringFilesException {
        Reply reply = control.sendCommand(Command.RENAME_FROM + fromPathName);

        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
            case ReplyCode.REQUESTED_FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
        }

        reply = control.sendCommand(Command.RENAME_TO + toPathName);

        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_NAME_NOT_ALLOWED:
                throw new FileNameNotAllowedException(reply.text);
            case ReplyCode.NEED_ACCOUNT_FOR_STORING_FILES:
                throw new NeedAccountForStoringFilesException(reply.text);
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
        Reply reply = control.sendCommand(Command.PASSIVE);

        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
        }

        int startIndex = reply.text.indexOf('(') + 1;
        int stopIndex = reply.text.indexOf(')');
        String hostPort = reply.text.substring(startIndex, stopIndex);
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

        Reply reply = control.sendCommand(Command.LIST + pathName);

        switch (reply.code) {
            case ReplyCode.REQUESTED_FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.REQUESTED_ACTION_ABORTED:
                throw new ActionAbortedException(reply.text);
        }

        ArrayList<RemoteFile> filesList = new ArrayList<RemoteFile>();

        for (String fileProperty : data.readLines()) {
            filesList.add(new RemoteFile(fileProperty));
        }

        reply = control.readReply();

        data.close();

        switch (reply.code) {
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.REQUESTED_ACTION_ABORTED:
                throw new ActionAbortedException(reply.text);
        }

        return filesList;
    }

    /**
     * This command causes the server-DTP to transfer a copy of the file, specified in the pathname,
     * to the user-DTP at the data connection.
     *
     * @param pathName path with name of file to retrieve.
     *                 (only name if file is in the current directory).
     * @return file as array of bytes.
     * @throws java.io.IOException                           If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException           If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException     If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException            If user not logged in.
     * @throws ftp.exception.FileActionNotTakenException     If file unavailable (e.g., file busy).
     * @throws ftp.exception.CantOpenDataConnectionException If data connection can't be opened.
     * @throws ftp.exception.ConnectionClosedException       If connection closed.
     * @throws ftp.exception.ActionAbortedException          If action aborted.
     * @throws ftp.exception.ActionNotTakenException         If action not taken.
     */
    public byte[] getFile(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            CantOpenDataConnectionException, ConnectionClosedException, ActionAbortedException,
            FileActionNotTakenException, ActionNotTakenException {
        byte[] file;

        openPassiveDTP();

        Reply reply = control.sendCommand(Command.RETRIEVE + pathName);

        switch (reply.code) {
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.REQUESTED_ACTION_ABORTED:
                throw new ActionAbortedException(reply.text);
            case ReplyCode.REQUESTED_FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
        }

        file = data.getBytes();

        reply = control.readReply();

        switch (reply.code) {
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.REQUESTED_ACTION_ABORTED:
                throw new ActionAbortedException(reply.text);
            case ReplyCode.REQUESTED_FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.REQUESTED_ACTION_NOT_TAKEN:
                throw new ActionNotTakenException(reply.text);
        }

        return file;
    }

}
