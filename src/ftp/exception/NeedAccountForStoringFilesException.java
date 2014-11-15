package ftp.exception;

/**
 * Indicates that user need account for storing files.
 *
 * @author Vojko Vladimir
 */
public class NeedAccountForStoringFilesException extends FTPException {

    public NeedAccountForStoringFilesException(String msg) {
        super(msg);
    }
}
