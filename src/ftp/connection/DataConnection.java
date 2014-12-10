package ftp.connection;

import ftp.Debugger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Data connection with server.
 *
 * @author Vojko Vladimir
 */
public class DataConnection {

    private static final int BLOCK_SIZE = 1024;
    private static final String DEBUG_TAG = "DATA";
    private static final String OPEN = "OPEN";
    private static final String CLOSE = "CLOSE";
    private static final String READ_LINES = "READ_LINES";
    private static final String GET_BYTES = "GET_BYTES: ";
    private static final String DATA_STREAM_ENDED_PREMATURELY = "Data stream ended prematurely";
    private static final String WRITE_BYTES = "WRITE_BYTES: ";

    private Socket passive;
    private InputStream dataIn;
    private OutputStream dataOut;
    private Debugger debugger;

    public DataConnection(Debugger debugger) {
        this.debugger = debugger;
    }

    /**
     * Open passive data connection with FTP server.
     *
     * @param host server address.
     * @param port server port.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void open(String host, int port)
            throws IOException {
        debugger.writeMassage(DEBUG_TAG, OPEN + " " + host + ":" + port);
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
        debugger.writeMassage(DEBUG_TAG, CLOSE);
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
        debugger.writeMassage(DEBUG_TAG, READ_LINES);
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataIn));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        close();
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
        debugger.writeMassage(DEBUG_TAG, GET_BYTES + size);
        byte[] bytes = new byte[size];
        int totalRead = 0;

        while (totalRead < size) {
            int bytesRead = dataIn.read(bytes, totalRead, size - totalRead);

            if (bytesRead < 0) {
                debugger.writeMassage(DEBUG_TAG, DATA_STREAM_ENDED_PREMATURELY);
                throw new IOException(DATA_STREAM_ENDED_PREMATURELY);
            }

            totalRead += bytesRead;

            if (listener != null) {
                listener.onBytesRead(size, totalRead);
            }
        }
        close();

        return bytes;
    }

    /**
     * Write bytes to data output stream.
     *
     * @param bytes    bytes to write.
     * @param listener bytes write listener.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeBytes(byte[] bytes, OnBytesWriteListener listener) throws IOException {
        debugger.writeMassage(DEBUG_TAG, WRITE_BYTES + bytes.length);
        int blocks = bytes.length / BLOCK_SIZE;
        int module = (bytes.length < BLOCK_SIZE) ? bytes.length : bytes.length % BLOCK_SIZE;
        int totalWrote = 0;

        for (int i = 0; i < blocks; i++) {
            dataOut.write(bytes, BLOCK_SIZE * i, BLOCK_SIZE);
            totalWrote += BLOCK_SIZE;
            if (listener != null) {
                listener.onBytesWrite(bytes.length, totalWrote);
            }
        }

        if (module != 0) {
            dataOut.write(bytes, BLOCK_SIZE * blocks, module);
            totalWrote += module;
            if (listener != null) {
                listener.onBytesWrite(bytes.length, totalWrote);
            }
        }

        dataOut.flush();
        close();
    }

}
