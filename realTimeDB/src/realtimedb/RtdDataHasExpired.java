/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdDataHasExpired extends RtdException {

    /**
     * Creates a new instance of <code>RtdDataHasExpired</code> without detail message.
     */
    public RtdDataHasExpired() {
    }

    /**
     * Constructs an instance of <code>RtdDataHasExpired</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RtdDataHasExpired(String msg) {
        super(msg);
    }
}
