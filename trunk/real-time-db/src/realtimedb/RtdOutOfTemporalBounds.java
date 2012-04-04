/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdOutOfTemporalBounds extends RtdException {

    /**
     * Creates a new instance of <code>RtdOutOfTemporalBounds</code> without detail message.
     */
    public RtdOutOfTemporalBounds() {
    }

    /**
     * Constructs an instance of <code>RtdOutOfTemporalBounds</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RtdOutOfTemporalBounds(String msg) {
        super(msg);
    }
}
