package ftp.connection;

/**
 * Codes of the FTP server replies.
 *
 * @author Vojko Vladimir
 */
public interface ReplyCode {
    int SERVICE_READY_IN_NNN_MINUTES = 120;
    int SERVICE_UNAVAILABLE = 421;
    int CANT_OPEN_DATA_CONNECTION = 425;
    int CONNECTION_CLOSED = 426;
    int FILE_ACTION_NOT_TAKEN = 450;
    int LOCAL_ERROR_IN_PROCESSING = 451;
    int INSUFFICIENT_STORAGE_SPACE = 452;
    int NOT_LOGGED_IN = 530;
    int NEED_ACCOUNT_FOR_STORING_FILES = 532;
    int FILE_UNAVAILABLE = 550;
    int UNKNOWN_PAGE_TYPE = 551;
    int FILE_ACTION_ABORTED = 552;
    int FILE_NAME_NOT_ALLOWED = 553;
}
