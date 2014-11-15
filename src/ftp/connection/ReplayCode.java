package ftp.connection;

/**
 * Codes of the FTP server replies.
 *
 * @author Vojko Vladimir
 */
public interface ReplayCode {
    int NOT_LOGGED_IN = 530;
    int SERVICE_UNAVAILABLE = 421;
    int SERVICE_READY_IN_NNN_MINUTES = 120;
    int REQUESTED_ACTION_NOT_TAKEN = 550;
    int REQUESTED_FILE_ACTION_NOT_TAKEN = 450;
    int CANT_OPEN_DATA_CONNECTION = 425;
    int CONNECTION_CLOSED = 426;
    int REQUESTED_ACTION_ABORTED = 451;
}
