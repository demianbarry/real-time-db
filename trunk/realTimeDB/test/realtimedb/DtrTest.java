/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author demian
 */
public class DtrTest {
    
    static protected DTRBaseContinuous<Float> cpuWorkLoad;
    static protected long maxEdgeCpuWorkLoad = 4000; // 4 secs of maximun adge
    
    static protected DTRBaseContinuous<Float> memWorkLoad;
    static protected long maxEdgeMemWorkLoad = 6000; // 6 secs of maximun adge
    
    static protected DTRBaseDiscrete<Float> loadTrend;
    
    static protected DtrDerived<Float> mesurableVal;
    
    static protected DTRBaseDiscrete<Byte> testByteDiscrete;
    
    public DtrTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Create all DTR objects. Asign objetcs to deriver DTR.
        cpuWorkLoad = new DTRBaseContinuous<Float>(maxEdgeCpuWorkLoad, new Float(50));
        memWorkLoad = new DTRBaseContinuous<Float>(maxEdgeMemWorkLoad, new Float(100));
        loadTrend = new DTRBaseDiscrete<Float>();
        
        mesurableVal = new DtrDerived<Float>();
        mesurableVal.addDTRReadSet(cpuWorkLoad);
        mesurableVal.addDTRReadSet(memWorkLoad);
        mesurableVal.addDTRReadSet(loadTrend);
        
        testByteDiscrete = new DTRBaseDiscrete<Byte>();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of application with DTR data having temporal restrictions.
     */
    @Test
    public void testValidDataDtr() {
        // validate data renge of cpuWorkLoad
        cpuWorkLoad.setMinValid(new Float(30));
        cpuWorkLoad.setMaxValid(new Float(50));
        try {
            cpuWorkLoad.setData(new Float(60));
        } catch (NotValidDtrData ex) {
            assertNotNull("Exception exit for not valid data for float type", ex);
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        try {
            cpuWorkLoad.setData(new Float(40));
            assertNotNull("Pass set valid data range for float", cpuWorkLoad);
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        
        // validate data range for byte test type 
        testByteDiscrete.setMinValid(new Byte((byte) 5));
        testByteDiscrete.setMinValid(new Byte((byte) 15));
        try {
            testByteDiscrete.setData(new Byte((byte) 25));
        } catch (NotValidDtrData ex) {
            assertNotNull("Exception exit for not valid data for byte type", ex);
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        try {
            testByteDiscrete.setData(new Byte((byte) 5));
            assertNotNull("Pass set valid data range for byte", testByteDiscrete);
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        try {
            testByteDiscrete.setData(new Byte((byte) 15));
            assertNotNull("Pass set valid data range for byte", testByteDiscrete);
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        

    }

    @Test
    public void testPrototypeDtr() throws InterruptedException {
        // initialice valida data 
        cpuWorkLoad.setMinValid(new Float(0));
        cpuWorkLoad.setMaxValid(new Float(100));
        memWorkLoad.setMinValid(new Float(0));
        memWorkLoad.setMaxValid(new Float(100));
        loadTrend.setMinValid(new Float(0));
        loadTrend.setMaxValid(new Float(100));

        // set initial time
        long initialTime = System.currentTimeMillis();
        
        // set input data
        try {
            cpuWorkLoad.setData(new Float(60));
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        try {
            memWorkLoad.setData(new Float(35));
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        try {
            loadTrend.setData(new Float(50));
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        
        // set derived data
        try {
            mesurableVal.setData(new Float(cpuWorkLoad.getData()*0.5 + memWorkLoad.getData()*0.3 + loadTrend.getData()*0.2));
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        
        // set current time
        long currentTime = System.currentTimeMillis();

        // print & verify time limits
        System.out.println("Test now: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
        System.out.println();
        
        // Sleep 3 sec, still valid
        Thread.sleep(3000);
        // set current time
        currentTime = System.currentTimeMillis();
        System.out.println("Test 3 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertTrue(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
        System.out.println();
        
        // Sleep 1 sec more, not valid for cpuWorkLoad and mesurableVal
        Thread.sleep(1000);
        // set current time
        currentTime = System.currentTimeMillis();
        System.out.println("Test 1 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            fail("cpuWorkLoad data has exired");
        } catch (DtrException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertTrue(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
            fail("mesurableVal data has exired");
        } catch (DtrException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");

        // Sleep 2 sec more, invalid data for all except loadTrend
        Thread.sleep(2000);
        // set current time
        currentTime = System.currentTimeMillis();
        System.out.println("Test 2 secs after: " + initialTime);
        System.out.println("Initial time seted: " + initialTime);
        System.out.println("Current time seted: " + currentTime);
        System.out.println("----------------------------------------");
        try {
            System.out.println("cpuWorkLoad data: " + cpuWorkLoad.getData());
            fail("cpuWorkLoad data has exired");
        } catch (DtrException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getValidityIntervalLowerBound() && currentTime <= cpuWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad data: " + memWorkLoad.getData());
            fail("memWorkLoad data has exired");
        } catch (DtrException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("memWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getValidityIntervalUpperBound());
        assertFalse(initialTime >= memWorkLoad.getValidityIntervalLowerBound() && currentTime <= memWorkLoad.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend data: " + loadTrend.getData());
        } catch (DtrException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getValidityIntervalUpperBound());
        assertTrue(initialTime >= loadTrend.getValidityIntervalLowerBound() && currentTime <= loadTrend.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal data: " + mesurableVal.getData());
            fail("mesurableVal data has exired");
        } catch (DtrException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getValidityIntervalLowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getValidityIntervalUpperBound());
        assertFalse(initialTime >= mesurableVal.getValidityIntervalLowerBound() && currentTime <= mesurableVal.getValidityIntervalUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
    
    }

}
