
package ch.poole.osm.josmfilterparser;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.poole.osm.josmfilterparser.Util;
import junit.framework.Assert;

/**
 * Tests for the OpeningHoursParser
 * 
 * @author Simon Poole
 *
 */
public class JosmFilterIntegrationTest {

    @Test
    public void regressionTest() {
        Condition c = parse("test1 | test2");
        Map<String,String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");
        
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    private Condition parse(String filterString) {

        try {
            JosmFilterParser parser = new JosmFilterParser(new ByteArrayInputStream(filterString.getBytes()));
            return parser.condition();
        } catch (ParseException pex) {
            System.out.println("Parser exception " + pex.toString());
        } catch (Error err) {
            System.out.println("Parser err " + err.toString());
        }
        return null;
    }
}
