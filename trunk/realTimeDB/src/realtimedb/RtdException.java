/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdException extends Exception {

    /**
     * Creates a new instance of <code>RtdException</code> without detail message.
     */
    public RtdException() {
    }

    /**
     * Constructs an instance of <code>RtdException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RtdException(String msg) {
        super(msg);
    }
}
