/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class DtrException extends Exception {

    /**
     * Creates a new instance of <code>DtrException</code> without detail message.
     */
    public DtrException() {
    }

    /**
     * Constructs an instance of <code>DtrException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DtrException(String msg) {
        super(msg);
    }
}
