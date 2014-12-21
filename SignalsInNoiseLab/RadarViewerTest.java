

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RadarViewerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RadarViewerTest
{
    /**
     * Default constructor for test class RadarViewerTest
     */
    public RadarViewerTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Test to see if the computer calculated slope is accurate
     */
    @Test
    public void testOutput()
    {
        final int ROWS = 100;
        final int COLS = 100;
        
        Radar radar = new Radar(5, 2);
        radar.setNoiseFraction(0.01);
        radar.scan();
        
        for(int i = 0; i < 200; i++)
        {
            radar.scan();
        }
        
        int[] realDxDy = radar.getDxDy();
        
        assertEquals(5, realDxDy[0]);
        assertEquals(2, realDxDy[1]);
    }
    
    /**
     * Test to see if the computer calculated slope is accurate
     */
    @Test
    public void testOutput2()
    {
        final int ROWS = 100;
        final int COLS = 100;
        
        Radar radar = new Radar(-4, 1);
        radar.setNoiseFraction(0.01);
        radar.scan();
        
        for(int i = 0; i < 200; i++)
        {
            radar.scan();
        }
        
        int[] realDxDy = radar.getDxDy();
        
        assertEquals(-4, realDxDy[0]);
        assertEquals(1, realDxDy[1]);
    }
}
