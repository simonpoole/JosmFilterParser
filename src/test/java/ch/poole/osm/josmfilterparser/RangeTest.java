
package ch.poole.osm.josmfilterparser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Range class
 * 
 * @author Simon Poole
 *
 */
public class RangeTest {

    /**
     * Test input to the Range class
     */
    @Test
    public void inputTest() {
        try {
            Range r = new WayLength("");
            fail("expected ParseException");
        } catch (ParseException pex) {
            // expected
        }
        
        try {
            Range r = new WayLength("A-1");
            fail("expected ParseException");
        } catch (ParseException pex) {
            // expected
        }

        try {
            Range r = new WayLength("1-A");
            fail("expected ParseException");
        } catch (ParseException pex) {
            // expected
        }
    }
}
