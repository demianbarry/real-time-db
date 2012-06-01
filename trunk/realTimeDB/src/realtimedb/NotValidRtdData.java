/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class NotValidRtdData extends RtdException {

    /**
     * Creates a new instance of <code>NotValidRtdData</code> without detail message.
     */
    public NotValidRtdData() {
    }

    /**
     * Constructs an instance of <code>NotValidRtdData</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NotValidRtdData(String msg) {
        super(msg);
    }
}
