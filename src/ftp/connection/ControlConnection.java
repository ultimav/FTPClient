package ftp.connection;

import ftp.Debugger;
import ftp.exception.NeedAccountException;
import ftp.exception.NoConnectionException;
import ftp.exception.NotLoggedInException;
import ftp.exception.ServiceUnavailableException;

import java.io.*;
import java.net.Socket;

/**
 * Mange control connection with the FTP server.
 * Execute commands and receive replies.
 *
 * @author Vojko Vladimir
 */
public class ControlConnection {

    private static final String DEBUG_TAG = "CONTROL";
    private static final String OPEN = "OPEN";
    private static final String CLOSE = "CLOSE";
    private static final String LOGIN = "LOGIN";
    private static final String REPLY = "REPLY: ";
    private static final String RESTORE_CONNECTION = "RESTORE_CONNECTION";
    private static final String SEND_COMMAND = "SEND_COMMAND: ";

    boolean connectionEstablished = false;
    boolean connected = false;
    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedReader reader = null;
    private String host = null;
    private int port = -1;
    private String user = null;
    private String pass = null;
    private Debugger debugger;

    public ControlConnection(Debugger debugger) {
        this.debugger = debugger;
    }

    /**
     * Establish control connection to the FTP server.
     *
     * @param host address of the host.
     * @param port port of the server to connect.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     */
    public void open(String host, int port)
            throws IOException, ServiceUnavailableException {
        if (connected) {
            close();
        }
        this.host = host;
        this.port = port;

        debugger.writeMassage(DEBUG_TAG, OPEN + " " + host + ":" + port);

        socket = new Socket(host, port);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Reply reply = readReply();

        debugger.writeMassage(DEBUG_TAG, REPLY + reply);

        switch (reply.code) {
            case ReplyCode.SERVICE_READY_IN_NNN_MINUTES:
            case ReplyCode.SERVICE_UNAVAILABLE:
                close();
                throw new ServiceUnavailableException(reply.text);
        }
        connected = true;
    }

    /**
     * Close control connection with FTP server.
     *
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void close()
            throws IOException {
        debugger.writeMassage(DEBUG_TAG, CLOSE);

        if (socket != null && socket.isConnected()) {
            socket.close();
            socket = null;
            writer = null;
            reader = null;
        }
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
            throws IOException, ServiceUnavailableException, NoConnectionException, NotLoggedInException,
            NeedAccountException {

        debugger.writeMassage(DEBUG_TAG, LOGIN);

        if (connected) {
            this.user = user;
            this.pass = pass;
            writer.print(Command.USER + user + "\r\n");
            writer.flush();
            Reply reply;
            reply = readReply();

            debugger.writeMassage(DEBUG_TAG, REPLY + reply);

            switch (reply.code) {
                case ReplyCode.SERVICE_UNAVAILABLE:
                    close();
                    throw new ServiceUnavailableException(reply.text);
                case ReplyCode.NOT_LOGGED_IN:
                    throw new NotLoggedInException(reply.text);
                case ReplyCode.NEED_ACCOUNT_FOR_LOGIN:
                    throw new NeedAccountException(reply.text);
            }

            writer.print(Command.PASS + pass + "\r\n");
            writer.flush();
            reply = readReply();

            debugger.writeMassage(DEBUG_TAG, REPLY + reply);

            switch (reply.code) {
                case ReplyCode.SERVICE_UNAVAILABLE:
                    close();
                    throw new ServiceUnavailableException(reply.text);
                case ReplyCode.NOT_LOGGED_IN:
                    throw new NotLoggedInException(reply.text);
            }
            connectionEstablished = true;
        } else {
            throw new NoConnectionException();
        }
    }

    public boolean isConnected() {
        try {
            while (hasUnreadReply()) {
                readReply();
            }
        } catch (IOException e) {
            connected = false;
        }
        return connected;
    }

    /**
     * Restore the connection if the time is up.
     *
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    private void restoreConnection()
            throws IOException, ServiceUnavailableException, NoConnectionException, NotLoggedInException,
            NeedAccountException {
        debugger.writeMassage(DEBUG_TAG, RESTORE_CONNECTION);

        open(host, port);
        login(user, pass);
    }

    /**
     * Reade reply from the server.
     *
     * @return server reply.
     * @throws IOException If an I/O error occurs.
     */
    public Reply readReply()
            throws IOException {
        Reply reply = new Reply();
        String replyText = reader.readLine();
        reply.code = Integer.parseInt(replyText.substring(0, 3));

        if (replyText.charAt(3) == '-') {
            StringBuilder replyTextBuilder = new StringBuilder(replyText.substring(4));
            while (!(replyText = reader.readLine()).contains(reply.code + " ")) {
                replyTextBuilder.append("\n").append(replyText);
            }
            reply.text = replyTextBuilder.toString();
        } else {
            reply.text = replyText.substring(3);
        }

        if (reply.code == ReplyCode.SERVICE_UNAVAILABLE) {
            connected = false;
            socket.close();
        }

        return reply;
    }

    /**
     * Send command to the FTP server.
     *
     * @param command command to send.
     * @return server reply.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     * @throws ftp.exception.NeedAccountException        If user need account for action.
     */
    public Reply sendCommand(String command)
            throws IOException, ServiceUnavailableException, NoConnectionException, NotLoggedInException,
            NeedAccountException {
        debugger.writeMassage(DEBUG_TAG, SEND_COMMAND + command);

        if (connected) {
            if (hasUnreadReply()) {
                Reply reply = readReply();
                if (reply.code == ReplyCode.SERVICE_UNAVAILABLE) {
                    if (connectionEstablished) {
                        restoreConnection();
                        return sendCommand(command);
                    } else {
                        throw new ServiceUnavailableException(reply.text);
                    }
                } else {
                    return sendCommand(command);
                }
            } else {
                writer.print(command + "\r\n");
                writer.flush();

                Reply reply = readReply();

                debugger.writeMassage(DEBUG_TAG, REPLY + reply);

                return reply;
            }
        } else {
            if (connectionEstablished) {
                restoreConnection();
                return sendCommand(command);
            } else {
                throw new NoConnectionException();
            }
        }
    }

    private boolean hasUnreadReply() throws IOException {
        return socket.getInputStream().available() > 0;
    }

}
