package ftp.connection;

/**
 * Commands of the FTP server.
 *
 * @author Vojko Vladimir
 */
public interface Command {
    String USER = "USER ";
    String PASS = "PASS ";
    String PASSIVE = "PASV";
    String PRINT_WORKING_DIRECTORY = "PWD";
    String CHANGE_WORKING_DIRECTORY = "CWD ";
    String CHANGE_TO_PARENT_DIRECTORY = "CDUP";
    String MAKE_DIRECTORY = "MKD ";
    String REMOVE_DIRECTORY = "RMD ";
    String LIST = "LIST ";
    String RENAME_FROM = "RNFR ";
    String RENAME_TO = "RNTO ";
    String RETRIEVE = "RETR ";
}
