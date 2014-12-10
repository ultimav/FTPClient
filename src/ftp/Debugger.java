package ftp;

/**
 * @author Vojko Vladimir
 */
public class Debugger {


    public interface DebugListener {
        void writeMessage(String message);
    }

    private DebugListener listener;

    public void writeMassage(String tag, String message) {
        if (listener != null) {
            listener.writeMessage(tag + "." + message);
        }
    }

    public void setListener(DebugListener listener) {
        this.listener = listener;
    }
}