/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

/**
 *
 * @author demian
 */
public class RtdBaseDiscrete<T extends Comparable<T>> extends RtdBase<T> {

    public RtdBaseDiscrete() {
        
    }

    @Override
    public void setValue(T value) throws RtdException {
        super.setValue(value);
        setVIUpperBound(Long.MAX_VALUE);
    }
}
