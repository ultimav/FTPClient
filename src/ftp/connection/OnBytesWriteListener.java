package ftp.connection;

/**
 * Interface definition for a callback to be invoked when written the bytes.
 *
 * @author Vojko Vladimir
 */
public interface OnBytesWriteListener {
    /**
     * Called when the bytes written.
     *
     * @param bytesAmount the amount of bytes to write.
     * @param totalWritten   the total amount of bytes written on current call.
     */
    public void onBytesWrite(int bytesAmount, int totalWritten);
}
