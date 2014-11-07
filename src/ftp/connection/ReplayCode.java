package ftp.connection;

/**
 * Codes of the FTP server replies.
 *
 * @author Vojko Vladimir
 */
public interface ReplayCode {
    int INCORRECT_LOGIN = 530;
    int SERVICE_UNAVAILABLE = 421;
    int SERVICE_READY_IN_NNN_MINUTES = 120;
}
