/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdNotValidData extends RtdException {

    /**
     * Creates a new instance of <code>RtdNotValidData</code> without detail message.
     */
    public RtdNotValidData() {
    }

    /**
     * Constructs an instance of <code>RtdNotValidData</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RtdNotValidData(String msg) {
        super(msg);
    }
}
