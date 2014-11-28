package ftp.connection;

/**
 * Interface definition for a callback to be invoked when the bytes read.
 *
 * @author Vojko Vladimir
 */
public interface OnBytesReadListener {
    /**
     * Called when the bytes read.
     *
     * @param bytesAmount the amount of bytes to read.
     * @param totalRead   the total amount of bytes read on current call.
     */
    public void onBytesRead(int bytesAmount, int totalRead);
}
