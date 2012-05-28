/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimedb;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author demian
 */
public class RtdTest {
    
    static protected RtdBaseContinuous<Float> cpuWorkLoad;
    static protected long maxEdgeCpuWorkLoad = 4000; // 4 secs of maximun adge
    
    static protected RtdBaseContinuous<Float> memWorkLoad;
    static protected long maxEdgeMemWorkLoad = 6000; // 6 secs of maximun adge
    
    static protected RtdBaseDiscrete<Float> loadTrend;
    
    static protected RtdDerived<Float> mesurableVal;
    
    static protected RtdBaseDiscrete<Byte> testByteDiscrete;
    
    public RtdTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Create all RTD objects. Asign objetcs to deriver RTD.
        cpuWorkLoad = new RtdBaseContinuous<Float>(maxEdgeCpuWorkLoad, new Float(50));
        memWorkLoad = new RtdBaseContinuous<Float>(maxEdgeMemWorkLoad, new Float(100));
        loadTrend = new RtdBaseDiscrete<Float>();
        
        mesurableVal = new RtdDerived<Float>();
        mesurableVal.addRtdReadSet(cpuWorkLoad);
        mesurableVal.addRtdReadSet(memWorkLoad);
        mesurableVal.addRtdReadSet(loadTrend);
        
        testByteDiscrete = new RtdBaseDiscrete<Byte>();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of application with RTD data having temporal restrictions.
     */
    @Test
    public void testValidDataRtd() {
        // validate value range of cpuWorkLoad
        cpuWorkLoad.setMinValid(new Float(30));
        cpuWorkLoad.setMaxValid(new Float(50));
        try {
            cpuWorkLoad.setValue(new Float(60));
            fail("Data not in valid range");
        } catch (RtdNotValidData ex) {
            assertNotNull("Exception exit for not valid value for float type", ex);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            cpuWorkLoad.setValue(new Float(40));
            assertNotNull("Pass set valid value range for float", cpuWorkLoad);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // validate value range for byte test type 
        testByteDiscrete.setMinValid(new Byte((byte) 5));
        testByteDiscrete.setMinValid(new Byte((byte) 15));
        try {
            testByteDiscrete.setValue(new Byte((byte) 25));
            //fail("Data not in valid range");
        } catch (RtdNotValidData ex) {
            assertNotNull("Exception exit for not valid value for byte type", ex);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            testByteDiscrete.setValue(new Byte((byte) 5));
            assertNotNull("Pass set valid value range for byte", testByteDiscrete);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            testByteDiscrete.setValue(new Byte((byte) 15));
            assertNotNull("Pass set valid value range for byte", testByteDiscrete);
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        

    }

    @Test
    public void testPrototypeRtd() throws InterruptedException {
        // initialice valida data 
        cpuWorkLoad.setMinValid(new Float(0));
        cpuWorkLoad.setMaxValid(new Float(100));
        memWorkLoad.setMinValid(new Float(0));
        memWorkLoad.setMaxValid(new Float(100));
        loadTrend.setMinValid(new Float(0));
        loadTrend.setMaxValid(new Float(100));
//        mesurableVal.setMinValid(new Float(0));

        // set initial time
        long initialTime = System.currentTimeMillis();
        
        // set input data
        try {
            cpuWorkLoad.setValue(new Float(60));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            memWorkLoad.setValue(new Float(35));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        try {
            loadTrend.setValue(new Float(50));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        
        // set derived data
        try {
            mesurableVal.setValue(new Float(cpuWorkLoad.getValue()*0.5 + memWorkLoad.getValue()*0.3 + loadTrend.getValue()*0.2));
        } catch (RtdException ex) {
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
            System.out.println("cpuWorkLoad value: " + cpuWorkLoad.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getVIUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getVILowerBound() && currentTime <= cpuWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad value: " + memWorkLoad.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getVIUpperBound());
        assertTrue(initialTime >= memWorkLoad.getVILowerBound() && currentTime <= memWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend value: " + loadTrend.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getVILowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getVIUpperBound());
        assertTrue(initialTime >= loadTrend.getVILowerBound() && currentTime <= loadTrend.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal value: " + mesurableVal.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getVILowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getVIUpperBound());
        assertTrue(initialTime >= mesurableVal.getVILowerBound() && currentTime <= mesurableVal.getVIUpperBound());
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
            System.out.println("cpuWorkLoad value: " + cpuWorkLoad.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getVIUpperBound());
        assertTrue(initialTime >= cpuWorkLoad.getVILowerBound() && currentTime <= cpuWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad value: " + memWorkLoad.getValue());
            assertTrue(new Float(35).equals(memWorkLoad.getValue()));
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getVIUpperBound());
        assertTrue(initialTime >= memWorkLoad.getVILowerBound() && currentTime <= memWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend value: " + loadTrend.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getVILowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getVIUpperBound());
        assertTrue(initialTime >= loadTrend.getVILowerBound() && currentTime <= loadTrend.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal value: " + mesurableVal.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getVILowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getVIUpperBound());
        assertTrue(initialTime >= mesurableVal.getVILowerBound() && currentTime <= mesurableVal.getVIUpperBound());
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
            System.out.println("cpuWorkLoad value: " + cpuWorkLoad.getValue());
            fail("cpuWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getVIUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getVILowerBound() && currentTime <= cpuWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad value: " + memWorkLoad.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getVIUpperBound());
        assertTrue(initialTime >= memWorkLoad.getVILowerBound() && currentTime <= memWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend value: " + loadTrend.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getVILowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getVIUpperBound());
        assertTrue(initialTime >= loadTrend.getVILowerBound() && currentTime <= loadTrend.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal value: " + mesurableVal.getValue());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getVILowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getVIUpperBound());
        assertFalse(initialTime >= mesurableVal.getVILowerBound() && currentTime <= mesurableVal.getVIUpperBound());
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
            System.out.println("cpuWorkLoad value: " + cpuWorkLoad.getValue());
            fail("cpuWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("cpuWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for cpuWorkLoad: " + cpuWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for cpuWorkLoad: " + cpuWorkLoad.getVIUpperBound());
        assertFalse(initialTime >= cpuWorkLoad.getVILowerBound() && currentTime <= cpuWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("memWorkLoad value: " + memWorkLoad.getValue());
            fail("memWorkLoad data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("memWorkLoad: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for memWorkLoad: " + memWorkLoad.getVILowerBound());
        System.out.println("Upper limit interval for memWorkLoad: " + memWorkLoad.getVIUpperBound());
        assertFalse(initialTime >= memWorkLoad.getVILowerBound() && currentTime <= memWorkLoad.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("loadTrend value: " + loadTrend.getValue());
        } catch (RtdException ex) {
            fail(ex.getMessage());
        }
        System.out.println("Lower limit interval for loadTrend: " + loadTrend.getVILowerBound());
        System.out.println("Upper limit interval for loadTrend: " + loadTrend.getVIUpperBound());
        assertTrue(initialTime >= loadTrend.getVILowerBound() && currentTime <= loadTrend.getVIUpperBound());
        System.out.println("----------------------------------------");
        try {
            System.out.println("mesurableVal value: " + mesurableVal.getValue());
            fail("mesurableVal data has exired");
        } catch (RtdException ex) {
            assertNotNull(ex.getMessage(), ex);
            System.out.println("mesurableVal: " + ex.getMessage());
        }
        System.out.println("Lower limit interval for mesurableVal: " + mesurableVal.getVILowerBound());
        System.out.println("Upper limit interval for mesurableVal: " + mesurableVal.getVIUpperBound());
        assertFalse(initialTime >= mesurableVal.getVILowerBound() && currentTime <= mesurableVal.getVIUpperBound());
        System.out.println("----------------------------------------");
        System.out.println("****************************************");
    
    }

}
