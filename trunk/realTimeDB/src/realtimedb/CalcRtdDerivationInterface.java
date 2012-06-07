package realtimedb;

/**
 * Interface for RTD Derivations implementations
 * 
 * Method "calc" implements derivation
 * Method "getWorstCaseExecutionTime" get WCET of calculus
 * 
 * @author Carlos Buckle
 */
public interface CalcRtdDerivationInterface {

    /**
     * Implements calc algorithm
     * Input parameter with RTD to derive
     */
    public void calc(RtdDerived d);

    /**
     * Return WCET of derivation
     */
    public long getWorstCaseExecutionTime();
}