package ftp.connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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

    /**
     * Read lines from data stream.
     *
     * @throws java.io.IOException If an I/O error occurs.
     */
    public ArrayList<String> readLines() throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataIn));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    /**
     * Read bytes from data stream.
     *
     * @param size     size of bytes to read.
     * @param listener bytes read listener.
     * @return read bytes.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public byte[] getBytes(int size, OnBytesReadListener listener) throws IOException {
        byte[] bytes = new byte[size];
        int totalRead = 0;

        while (totalRead < size) {
            int bytesRead = dataIn.read(bytes, totalRead, size - totalRead);

            if (bytesRead < 0) {
                throw new IOException("Data stream ended prematurely");
            }

            totalRead += bytesRead;

            if (listener != null) {
                listener.onBytesRead(size, totalRead);
            }
        }

        return bytes;
    }

}
