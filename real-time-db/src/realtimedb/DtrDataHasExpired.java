/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class DtrDataHasExpired extends DtrException {

    /**
     * Creates a new instance of <code>DtrDataHasExpired</code> without detail message.
     */
    public DtrDataHasExpired() {
    }

    /**
     * Constructs an instance of <code>DtrDataHasExpired</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DtrDataHasExpired(String msg) {
        super(msg);
    }
}
