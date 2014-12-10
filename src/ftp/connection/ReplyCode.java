package ftp.connection;

/**
 * Codes of the FTP server replies.
 *
 * @author Vojko Vladimir
 */
public interface ReplyCode {
    /**
     * Indicates that service will be ready in nnn minutes.
     */
    int SERVICE_READY_IN_NNN_MINUTES = 120;
    /**
     * Need account for login.
     */
    int NEED_ACCOUNT_FOR_LOGIN = 332;
    /**
     * Indicates that service not available, closing control connection.
     * This may be a reply to any command if the service knows it must shut down.
     */
    int SERVICE_UNAVAILABLE = 421;
    /**
     * Indicates that service can't open data connection.
     */
    int CANT_OPEN_DATA_CONNECTION = 425;
    /**
     * Indicates that service closing data connection.
     * Requested file action successful (for example, file transfer or file abort).
     */
    int CONNECTION_CLOSED = 426;
    /**
     * Indicates that requested file action not taken.
     * File unavailable (e.g., file busy).
     */
    int FILE_ACTION_NOT_TAKEN = 450;
    /**
     * Indicates that requested action aborted: local error in processing.
     */
    int LOCAL_ERROR_IN_PROCESSING = 451;
    /**
     * Indicates that requested action not taken.
     * Insufficient storage space in system.
     */
    int INSUFFICIENT_STORAGE_SPACE = 452;
    /**
     * Syntax error, command unrecognized.
     * This may include errors such as command line too long.
     */
    int SYNTAX_ERROR = 500;
    /**
     * Syntax error in parameters or arguments.
     */
    int SYNTAX_ERROR_IN_PARAMS_OR_ARGS = 501;
    /**
     * Command not implemented.
     */
    int COMMAND_NOT_IMPLEMENTED = 502;
    /**
     * Bad sequence of commands.
     */
    int BAD_SEQUENCE_OF_COMMANDS = 503;
    /**
     * Command not implemented for that parameter.
     */
    int COMMAND_NOT_IMPLEMENTED_FOR_PARAM = 504;
    /**
     * Indicates that user not logged in.
     */
    int NOT_LOGGED_IN = 530;
    /**
     * Indicates that user need account for storing files.
     */
    int NEED_ACCOUNT_FOR_STORING_FILES = 532;
    /**
     * Indicates that requested action not taken.
     * File unavailable (e.g., file not found, no access).
     */
    int FILE_UNAVAILABLE = 550;
    /**
     * Indicates that requested action aborted: page type unknown.
     */
    int UNKNOWN_PAGE_TYPE = 551;
    /**
     * Indicates that requested file action aborted.
     * Exceeded storage allocation (for current directory or dataset).
     */
    int FILE_ACTION_ABORTED = 552;
    /**
     * Indicates that requested action not taken.
     * File name not allowed.
     */
    int FILE_NAME_NOT_ALLOWED = 553;
}
