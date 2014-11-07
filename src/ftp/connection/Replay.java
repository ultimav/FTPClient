package ftp.connection;

/**
 * The model that represents the message replies from FTP server.
 *
 * @author Vojko Vladimir
 */
public class Replay {

    public int code;
    public String text;

    @Override
    public String toString() {
        return code + text;
    }
}
