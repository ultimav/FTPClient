package ftp.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Data connection with server.
 *
 * @author Vojko Vladimir
 */
public class DataConnection {

    private Socket passive;
    private InputStream dataIn;
    private OutputStream dataOut;

    /**
     * Open passive data connection with FTP server.
     *
     * @param host server address.
     * @param port server port.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void open(String host, int port)
            throws IOException {
        passive = new Socket(host, port);
        dataIn = passive.getInputStream();
        dataOut = passive.getOutputStream();
    }

    /**
     * Close data connection.
     *
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void close() throws IOException {
        if (passive != null && passive.isConnected()) {
            passive.close();
            passive = null;
            dataIn = null;
            dataOut = null;
        }
    }

}
