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
    private Debugger debugger;

    /**
     * Create FTP Client.
     */
    public FTPClient() {
        debugger = new Debugger();
        control = new ControlConnection(debugger);
        data = new DataConnection(debugger);
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
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void login(String user, String pass)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            NeedAccountException {
        control.login(user, pass);
    }


    public boolean isConnected() {
        return control.isConnected();
    }

    /**
     * Get the name of the current working directory.
     *
     * @return path with name of the current working directory.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public String printWorkingDirectory()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            NeedAccountException {
        Reply reply = control.sendCommand(Command.PRINT_WORKING_DIRECTORY);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
        }
        int startIndex = reply.text.indexOf('"') + 1;
        int stopIndex = reply.text.indexOf('"', startIndex);
        return reply.text.substring(startIndex, stopIndex);
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
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void changeWorkingDirectory(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException, NeedAccountException {
        Reply reply = control.sendCommand(Command.CHANGE_WORKING_DIRECTORY + pathName);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
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
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void changeToParentDirectory()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException, NeedAccountException {
        Reply reply = control.sendCommand(Command.CHANGE_TO_PARENT_DIRECTORY);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
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
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void makeDirectory(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException, NeedAccountException {
        Reply reply = control.sendCommand(Command.MAKE_DIRECTORY + pathName);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
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
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void removeDirectory(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException, NeedAccountException {
        Reply reply = control.sendCommand(Command.REMOVE_DIRECTORY + pathName);
        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
        }
    }

    /**
     * Rename file.
     *
     * @param fromPathName old file name (with full path, or without if file is in current working directory).
     * @param toPathName   new file name (with full path, or without if file is in current working directory).
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.ActionNotTakenException     If action not taken.
     * @throws ftp.exception.FileActionNotTakenException If file unavailable (e.g., file busy).
     * @throws ftp.exception.FileNameNotAllowedException If file nam is not allowed.
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void renameFromTo(String fromPathName, String toPathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            ActionNotTakenException, FileActionNotTakenException, FileNameNotAllowedException,
            NeedAccountException {
        Reply reply = control.sendCommand(Command.RENAME_FROM + fromPathName);

        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
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
                throw new NeedAccountException(reply.text);
        }
    }

    /**
     * Open data connection in the passive mode.
     *
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    private void openPassiveDTP()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            NeedAccountException {
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
     * @throws ftp.exception.NeedAccountException            If user need account for action.
     */
    public ArrayList<RemoteFile> getFilesList()
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            FileActionNotTakenException, CantOpenDataConnectionException, ConnectionClosedException,
            ActionAbortedException, NeedAccountException {
        return getFilesList(null);
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
     * @throws ftp.exception.NeedAccountException            If user need account for action.
     */
    public ArrayList<RemoteFile> getFilesList(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            FileActionNotTakenException, CantOpenDataConnectionException, ConnectionClosedException,
            ActionAbortedException, NeedAccountException {
        openPassiveDTP();

        String listCommand = (pathName == null) ? Command.LIST : Command.LIST + " " + pathName;
        Reply reply = control.sendCommand(listCommand);

        switch (reply.code) {
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.LOCAL_ERROR_IN_PROCESSING:
                throw new LocalErrorInProcessingException(reply.text);
        }

        ArrayList<RemoteFile> filesList = new ArrayList<RemoteFile>();

        for (String fileProperty : data.readLines()) {
            filesList.add(new RemoteFile(fileProperty));
        }

        reply = control.readReply();

        switch (reply.code) {
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.LOCAL_ERROR_IN_PROCESSING:
                throw new LocalErrorInProcessingException(reply.text);
        }

        return filesList;
    }

    /**
     * This command causes the server-DTP to transfer a copy of the file, specified in the pathname,
     * to the user-DTP at the data connection.
     *
     * @param pathName path with name of file to retrieve.
     *                 (only name if file is in the current directory).
     * @param listener bytes read listener.
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
     * @throws ftp.exception.NeedAccountException            If user need account for action.
     */
    public byte[] getFile(String pathName, OnBytesReadListener listener)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            CantOpenDataConnectionException, ConnectionClosedException, ActionAbortedException,
            FileActionNotTakenException, ActionNotTakenException, NeedAccountException {
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
            case ReplyCode.LOCAL_ERROR_IN_PROCESSING:
                throw new LocalErrorInProcessingException(reply.text);
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
        }

        int startIndex = reply.text.lastIndexOf('(') + 1;
        int stopIndex = reply.text.lastIndexOf(" bytes)");
        int size = Integer.parseInt(reply.text.substring(startIndex, stopIndex));

        file = data.getBytes(size, listener);

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
            case ReplyCode.LOCAL_ERROR_IN_PROCESSING:
                throw new LocalErrorInProcessingException(reply.text);
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
        }

        return file;
    }

    /**
     * This command causes the server-DTP to accept the data transferred via the data connection and to store
     * the data as a file at the server site. If the file specified in the pathname exists at the server site,
     * then its contents shall be replaced by the data being transferred. A new file is created
     * at the server site if the file specified in the pathname does not already exist.
     *
     * @param file     file to send as array og bytes.
     * @param pathName path with name of file to store.
     *                 (only name if file is in the current directory).
     * @param listener bytes write listener.
     * @throws java.io.IOException                             If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException             If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException       If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException              If user not logged in.
     * @throws ftp.exception.CantOpenDataConnectionException   If data connection can't be opened.
     * @throws ftp.exception.ConnectionClosedException         If connection closed.
     * @throws ftp.exception.LocalErrorInProcessingException   If there is local error in processing.
     * @throws ftp.exception.PageTypeUnknownException          If page type unknown.
     * @throws ftp.exception.FileActionAbortedException        If file action is aborted, because exceeded
     *                                                         storage allocation.
     * @throws ftp.exception.FileActionNotTakenException       If file is unavailable (e.g., file busy).
     * @throws ftp.exception.NeedAccountException              If user need account for action.
     * @throws ftp.exception.InsufficientStorageSpaceException If there is insufficient storage space in system.
     * @throws ftp.exception.FileNameNotAllowedException       If filename is not allowed.
     */
    public void sendFile(byte[] file, String pathName, OnBytesWriteListener listener)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            CantOpenDataConnectionException, ConnectionClosedException, LocalErrorInProcessingException,
            PageTypeUnknownException, FileActionAbortedException, FileActionNotTakenException,
            NeedAccountException, InsufficientStorageSpaceException, FileNameNotAllowedException {
        openPassiveDTP();

        Reply reply = control.sendCommand(Command.STORE + pathName);

        switch (reply.code) {
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.CANT_OPEN_DATA_CONNECTION:
                throw new CantOpenDataConnectionException(reply.text);
            case ReplyCode.CONNECTION_CLOSED:
                throw new ConnectionClosedException(reply.text);
            case ReplyCode.LOCAL_ERROR_IN_PROCESSING:
                throw new LocalErrorInProcessingException(reply.text);
            case ReplyCode.UNKNOWN_PAGE_TYPE:
                throw new PageTypeUnknownException(reply.text);
            case ReplyCode.FILE_ACTION_ABORTED:
                throw new FileActionAbortedException(reply.text);
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.NEED_ACCOUNT_FOR_STORING_FILES:
                throw new NeedAccountException(reply.text);
            case ReplyCode.INSUFFICIENT_STORAGE_SPACE:
                throw new InsufficientStorageSpaceException(reply.text);
            case ReplyCode.FILE_NAME_NOT_ALLOWED:
                throw new FileNameNotAllowedException(reply.text);
        }

        data.writeBytes(file, listener);

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
            case ReplyCode.LOCAL_ERROR_IN_PROCESSING:
                throw new LocalErrorInProcessingException(reply.text);
            case ReplyCode.UNKNOWN_PAGE_TYPE:
                throw new PageTypeUnknownException(reply.text);
            case ReplyCode.FILE_ACTION_ABORTED:
                throw new FileActionAbortedException(reply.text);
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.NEED_ACCOUNT_FOR_STORING_FILES:
                throw new NeedAccountException(reply.text);
            case ReplyCode.INSUFFICIENT_STORAGE_SPACE:
                throw new InsufficientStorageSpaceException(reply.text);
            case ReplyCode.FILE_NAME_NOT_ALLOWED:
                throw new FileNameNotAllowedException(reply.text);
        }

    }

    /**
     * This command causes the file specified in the pathname to be deleted at the server site.
     *
     * @param pathName path with name of file to delete.
     *                 (only name if file is in the current directory).
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.FileActionNotTakenException If file unavailable (e.g., file busy).
     * @throws ftp.exception.FileUnavailableException    If file unavailable (e.g., file not found, no access).
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public void deleteFile(String pathName)
            throws IOException, NoConnectionException, ServiceUnavailableException, NotLoggedInException,
            FileActionNotTakenException, FileUnavailableException, NeedAccountException {
        Reply reply = control.sendCommand(Command.DELETE + pathName);

        switch (reply.code) {
            case ReplyCode.SERVICE_UNAVAILABLE:
                throw new ServiceUnavailableException(reply.text);
            case ReplyCode.NOT_LOGGED_IN:
                throw new NotLoggedInException(reply.text);
            case ReplyCode.FILE_ACTION_NOT_TAKEN:
                throw new FileActionNotTakenException(reply.text);
            case ReplyCode.FILE_UNAVAILABLE:
                throw new FileUnavailableException(reply.text);
        }
    }

    public void setDebugListener(Debugger.DebugListener listener) {
        debugger.setListener(listener);
    }

}
