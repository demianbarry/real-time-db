/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class NotValidDtrData extends DtrException {

    /**
     * Creates a new instance of <code>NotValidDtrData</code> without detail message.
     */
    public NotValidDtrData() {
    }

    /**
     * Constructs an instance of <code>NotValidDtrData</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NotValidDtrData(String msg) {
        super(msg);
    }
}
