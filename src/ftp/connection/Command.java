package ftp.connection;

/**
 * Commands of the FTP server.
 * @author Vojko Vladimir
 */
public interface Command {
    String USER = "USER ";
    String PASS = "PASS ";
    String PASSIVE = "PASV";
    String LIST = "LIST";
}
