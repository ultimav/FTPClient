package ftp.exception;

/**
 * Indicates that the user has entered an incorrect username or password.
 *
 * @author Vojko Vladimir
 */
public class LoginIncorrectException extends FTPException {

    public LoginIncorrectException(String msg) {
        super(msg);
    }
}
