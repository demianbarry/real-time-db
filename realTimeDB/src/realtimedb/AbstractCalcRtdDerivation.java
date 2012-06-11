package realtimedb;

/**
 * Interface for RTD Derivations implementations
 * 
 * Method "calc" implements derivation
 * Method "getWorstCaseExecutionTime" get WCET of calculus
 * 
 * @author Carlos Buckle
 */
abstract public class AbstractCalcRtdDerivation {
    private long worstCaseExecutionTime = Long.valueOf(0);

    /**
     * Implements calc algorithm
     * Input parameter with RTD to derive
     */
    abstract public void calc(RtdDerived d);
    
        /**
     * Return WCET of derivation
     */
    public long getWorstCaseExecutionTime(){
        return this.worstCaseExecutionTime;
    }
    
    /**
     * Set WCET for derivation
     */
    public void setWorstCaseExecutionTime(long worstCaseExecutionTime){
        this.worstCaseExecutionTime = worstCaseExecutionTime;
    }
}