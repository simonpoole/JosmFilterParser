
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the OpeningHoursParser
 * 
 * @author Simon Poole
 *
 */
public class JosmFilterIntegrationTest {

    @Test
    public void matchTest() {

        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("test1=*");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1=grrr");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        
        c = parse("*=grrr");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        
        tags.clear();
        tags.put("test1", "");
        c = parse("test1=");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }
    
    @Test
    public void orTest() {

        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("test1 | test2");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1=grrr | test2=grrr");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        
        c = parse("test1 | test");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    @Test
    public void andTest() {
        Condition c = parse("test1 test2");
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        tags.put("test2", "grrr");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    @Test
    public void typeTest() {
        Condition c = parse("type:node");
        Map<String, String> tags = new HashMap<>();

        Assert.assertFalse(c.eval(Type.WAY, null, tags));
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    @Test
    public void valueFragmentTest() {
        Condition c = parse("test1:rr");
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("type1:aa");
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("tes1:rr");
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("-test1:rr");
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
    }

    @Test
    public void idTest() {
        Condition c = parse("id:123");
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.id = 123;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        c = parse("id:321");
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }
    
    @Test
    public void changesetTest() {
        Condition c = parse("changeset:123");
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.changeset = 123;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        c = parse("changeset:321");
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }

    @Test
    public void versionTest() {
        Condition c = parse("version:123");
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.version = 123;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        c = parse("version:321");
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }

    @Test
    public void stateTest() {
        Condition c = parse("modified");
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.state = ElementState.State.MODIFIED;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
    }
    
    @Test
    public void closedTest() {
        Condition c = parse("closed");
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.isClosed = true;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        meta.isClosed = false;
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }
    
    @Test
    public void tagsTest() {
        Condition c = parse("tags:1");
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");
        
        Assert.assertFalse(c.eval(Type.WAY, null, tags));
        
        c = parse("tags:2");
        Assert.assertTrue(c.eval(Type.WAY, null, tags));
        
        c = parse("tags:1-2");
        Assert.assertTrue(c.eval(Type.WAY, null, tags));
        
        c = parse("tags:-3");
        Assert.assertTrue(c.eval(Type.WAY, null, tags));
        
        c = parse("tags:1-");
        Assert.assertTrue(c.eval(Type.WAY, null, tags));
        
        c = parse("tags:0");
        Assert.assertTrue(c.eval(Type.WAY, null, null));
    }   
    
    @Test
    public void untaggedTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");
        
        Condition c = parse("untagged");
        Assert.assertFalse(c.eval(Type.WAY, null, tags));
        
        tags.clear();
        Assert.assertTrue(c.eval(Type.WAY, null, tags));
        
        Assert.assertTrue(c.eval(Type.WAY, null, null));
    }   
    
    @Test
    public void nodesTest() {
        TestMeta meta = new TestMeta();
        meta.nodeCount = 2;
        Condition c = parse("nodes:1");
        
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
        
        c = parse("nodes:2");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("nodes:1-2");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("nodes:-3");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("nodes:1-");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }   
    
    @Test
    public void waysTest() {
        TestMeta meta = new TestMeta();
        meta.wayCount = 2;
        Condition c = parse("ways:1");
        
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
        
        c = parse("ways:2");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("ways:1-2");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("ways:-3");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("ways:1-");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }  
    
    @Test
    public void wayLengthTest() {
        TestMeta meta = new TestMeta();
        meta.wayLength = 20;
        Condition c = parse("waylength:1");
        
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
        
        c = parse("waylength:20");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("waylength:10-20");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("waylength:-30");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("waylength:10-");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }   
    
    @Test
    public void areaSizeTest() {
        TestMeta meta = new TestMeta();
        meta.areaSize = 200;
        Condition c = parse("areasize:100");
        
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
        
        c = parse("areasize:200");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("areasize:100-200");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("areasize:-300");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("areasize:100-");
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }   
    
    @Test
    public void timestampTest() {
        TestMeta meta = new TestMeta();
        meta.timestamp = ElementTimestamp.parseDateTime("2005-10-01");
        Condition c = parse("timestamp:2004-1-5T14:00/2010");
        
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("timestamp:2011/");
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }   

    @Test
    public void roleTest() {
        TestMeta meta = new TestMeta();
        meta.roles.add("stop");
        Condition c = parse("role:stop");
        
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("role:false");
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }   
    
    @Test
    public void userTest() {
        TestMeta meta = new TestMeta();
        meta.user = "SimonPoole";
        Condition c = parse("user:SimonPoole");
        
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
        
        c = parse("user:PooleSimon");
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }   
    
    @Test
    public void parentheseTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");
        
        Condition c = parse("(test1 test2) | test3");
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        
        c = parse("(test1 | test2)  test");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1 (test2 | test)");
        System.err.println(c.toString());
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }   
    
    @Test
    public void affinityTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");
        
        Condition c = parse("test1 test2 | test3");
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        c = parse("test1 test2 | test");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        c = parse("test1 | test2 test");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }   
    
    @Test
    public void notTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");
        
        Condition c = parse("-test1");
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        
        c = parse("-test2");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("-(test2 | test3)");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }   
    
    private Condition parse(String filterString) {

        try {
            JosmFilterParser parser = new JosmFilterParser(new ByteArrayInputStream(filterString.getBytes()));
            return parser.condition();
        } catch (ParseException pex) {
            System.err.println("Parser exception " + pex.toString());
            Assert.fail(pex.toString());
        } catch (Error err) {
            System.err.println("Parser err " + err.toString());
            Assert.fail(err.toString());
        }
        return null;
    }
}
