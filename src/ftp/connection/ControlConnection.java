package ftp.connection;

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

    boolean connectionEstablished = false;
    boolean connected = false;
    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedReader reader = null;
    private String host = null;
    private int port = -1;
    private String user = null;
    private String pass = null;

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
        socket = new Socket(host, port);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Replay replay = readReplay();
        switch (replay.code) {
            case ReplayCode.SERVICE_READY_IN_NNN_MINUTES:
            case ReplayCode.SERVICE_UNAVAILABLE:
                close();
                throw new ServiceUnavailableException(replay.text);
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
     */
    public void login(String user, String pass)
            throws IOException, ServiceUnavailableException, NoConnectionException, NotLoggedInException {
        if (connected) {
            this.user = user;
            this.pass = pass;
            writer.println(Command.USER + user);
            writer.println(Command.PASS + pass);
            writer.flush();
            Replay replay;
            replay = readReplay();
            switch (replay.code) {
                case ReplayCode.SERVICE_UNAVAILABLE:
                    close();
                    throw new ServiceUnavailableException(replay.text);
            }
            replay = readReplay();
            switch (replay.code) {
                case ReplayCode.SERVICE_UNAVAILABLE:
                    close();
                    throw new ServiceUnavailableException(replay.text);
                case ReplayCode.NOT_LOGGED_IN:
                    throw new NotLoggedInException(replay.text);
            }
            connectionEstablished = true;
        } else {
            throw new NoConnectionException();
        }
    }

    /**
     * Restore the connection if the time is up.
     *
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     */
    private void restoreConnection()
            throws IOException, ServiceUnavailableException, NoConnectionException, NotLoggedInException {
        open(host, port);
        login(user, pass);
    }

    /**
     * Reade reply from the server.
     *
     * @return server replay.
     * @throws IOException If an I/O error occurs.
     */
    public Replay readReplay()
            throws IOException {
        Replay replay = new Replay();
        String replayText = reader.readLine();
        replay.code = Integer.parseInt(replayText.substring(0, 3));

        if (replayText.charAt(3) == '-') {
            StringBuilder replayTextBuilder = new StringBuilder(replayText.substring(4));
            while (!(replayText = reader.readLine()).contains(replay.code + " ")) {
                replayTextBuilder.append("\n").append(replayText);
            }
            replay.text = replayTextBuilder.toString();
        } else {
            replay.text = replayText.substring(3);
        }

        if (replay.code == ReplayCode.SERVICE_UNAVAILABLE) {
            connected = false;
        }

        // FOR DEBUG
        System.out.println(replay);
        return replay;
    }

    /**
     * Send command to the FTP server.
     *
     * @param command command to send.
     * @return server replay.
     * @throws java.io.IOException                       If an I/O error occurs.
     * @throws ftp.exception.NoConnectionException       If there is no connection.
     * @throws ftp.exception.ServiceUnavailableException If ftp server is unavailable.
     * @throws ftp.exception.NotLoggedInException        If user not logged in.
     */
    public Replay sendCommand(String command)
            throws IOException, ServiceUnavailableException, NoConnectionException, NotLoggedInException {
        if (connected) {
            if (socket.getInputStream().available() > 0) {
                Replay replay = readReplay();
                if (replay.code == ReplayCode.SERVICE_UNAVAILABLE) {
                    if (connectionEstablished) {
                        restoreConnection();
                        return sendCommand(command);
                    } else {
                        throw new ServiceUnavailableException(replay.text);
                    }
                } else {
                    return sendCommand(command);
                }
            } else {
                writer.println(command);
                writer.flush();
                return readReplay();
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

}
