/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class DataPrecisionErrorDtr extends DtrException {

    /**
     * Creates a new instance of <code>DataPrecisionErrorDtr</code> without detail message.
     */
    public DataPrecisionErrorDtr() {
    }

    /**
     * Constructs an instance of <code>DataPrecisionErrorDtr</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DataPrecisionErrorDtr(String msg) {
        super(msg);
    }
}
