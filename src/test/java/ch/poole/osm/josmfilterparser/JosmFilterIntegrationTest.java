
package ch.poole.osm.josmfilterparser;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the OpeningHoursParser
 * 
 * @author Simon Poole
 *
 */
public class JosmFilterIntegrationTest {

    /**
     * Test normal tag matching
     */
    @Test
    public void matchTest() {

        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("test1=*", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1=grrr", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("*=grrr", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        tags.clear();
        tags.put("test1", "");
        c = parse("test1=", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        Assert.assertFalse(c.eval(Type.NODE, null, null));
    }

    /**
     * Test tag matching with regexps
     */
    @Test
    public void matchRegexpTest() {

        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("test1=.*", true);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("te+st1=g..r", true);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1=grRr", true);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("tes.1", true);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("gr[ri]r", true);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("Test1", true);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse(".*=grrr", true);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        tags.clear();
        tags.put("test1", "");
        c = parse("test1=", true);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Test alphanumeric value comparisons
     */
    @Test
    public void valueComparisionTest() {

        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "123");
        tags.put("test", "2004-12-01");

        // numerical
        Condition c = parse("test1<200", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        c = parse("test1<100", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        c = parse("test1>100", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        c = parse("test1>200", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        // alphanumberical
        c = parse("test<\"2005-01-01\"", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        c = parse("test<\"2004-01-01\"", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        c = parse("test>\"2005-01-01\"", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        c = parse("test>\"2004-01-01\"", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        // other stuff
        c = parse("test2<100", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        c = parse("test<100", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
    }

    /**
     * Test logical OR of conditions
     */
    @Test
    public void orTest() {

        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("test1 | test2", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1=grrr | test2=grrr", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1 | test", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Test logical AND of conditions
     */
    @Test
    public void andTest() {
        Condition c = parse("test1 test2", false);
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        tags.put("test2", "grrr");
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Test testing for type of OSM element
     */
    @Test
    public void typeTest() {
        Condition c = parse("type:node", false);
        Map<String, String> tags = new HashMap<>();

        Assert.assertFalse(c.eval(Type.WAY, null, tags));
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Test value fragment matching
     */
    @Test
    public void valueFragmentTest() {
        Condition c = parse("test1:rr", false);
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("type1:aa", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("tes1:rr", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("-test1:rr", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
    }

    /**
     * Test OSM id matching
     */
    @Test
    public void idTest() {
        Condition c = parse("id:123", false);
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.id = 123;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        c = parse("id:321", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }

    /**
     * Test changeset id matching
     */
    @Test
    public void changesetTest() {
        Condition c = parse("changeset:123", false);
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.changeset = 123;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        c = parse("changeset:321", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }

    /**
     * Test version number matching
     */
    @Test
    public void versionTest() {
        Condition c = parse("version:123", false);
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.version = 123;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        c = parse("version:321", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }

    /**
     * Test OSM element state matching
     */
    @Test
    public void stateTest() {
        Condition c = parse("modified", false);
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.state = ElementState.State.MODIFIED;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
    }

    /**
     * Test checking for a closed way
     */
    @Test
    public void closedTest() {
        Condition c = parse("closed", false);
        Map<String, String> tags = new HashMap<>();
        TestMeta meta = new TestMeta();
        meta.isClosed = true;
        Assert.assertTrue(c.eval(Type.WAY, meta, tags));
        meta.isClosed = false;
        Assert.assertFalse(c.eval(Type.WAY, meta, tags));
    }

    /**
     * Test tag counts matching
     */
    @Test
    public void tagsTest() {
        Condition c = parse("tags:1", false);
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Assert.assertFalse(c.eval(Type.WAY, null, tags));

        c = parse("tags:2", false);
        Assert.assertTrue(c.eval(Type.WAY, null, tags));

        c = parse("tags:1-2", false);
        Assert.assertTrue(c.eval(Type.WAY, null, tags));

        c = parse("tags:-3", false);
        Assert.assertTrue(c.eval(Type.WAY, null, tags));

        c = parse("tags:1-", false);
        Assert.assertTrue(c.eval(Type.WAY, null, tags));

        c = parse("tags:0", false);
        Assert.assertTrue(c.eval(Type.WAY, null, null));
    }

    /**
     * Test checking for an untagged element
     */
    @Test
    public void untaggedTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("untagged", false);
        Assert.assertFalse(c.eval(Type.WAY, null, tags));

        tags.clear();
        Assert.assertTrue(c.eval(Type.WAY, null, tags));

        Assert.assertTrue(c.eval(Type.WAY, null, null));
    }

    /**
     * Test checking for number of way nodes
     */
    @Test
    public void nodesTest() {
        TestMeta meta = new TestMeta();
        meta.nodeCount = 2;
        Condition c = parse("nodes:1", false);

        Assert.assertFalse(c.eval(Type.WAY, meta, null));

        c = parse("nodes:2", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("nodes:1-2", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("nodes:-3", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("nodes:1-", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test for way membership count matching
     */
    @Test
    public void waysTest() {
        TestMeta meta = new TestMeta();
        meta.wayCount = 2;
        Condition c = parse("ways:1", false);

        Assert.assertFalse(c.eval(Type.WAY, meta, null));

        c = parse("ways:2", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("ways:1-2", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("ways:-3", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("ways:1-", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test for way length matching
     */
    @Test
    public void wayLengthTest() {
        TestMeta meta = new TestMeta();
        meta.wayLength = 20;
        Condition c = parse("waylength:1", false);

        Assert.assertFalse(c.eval(Type.WAY, meta, null));

        c = parse("waylength:20", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("waylength:10-20", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("waylength:-30", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("waylength:10-", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test for area size matching
     */
    @Test
    public void areaSizeTest() {
        TestMeta meta = new TestMeta();
        meta.areaSize = 200;
        Condition c = parse("areasize:100", false);

        Assert.assertFalse(c.eval(Type.WAY, meta, null));

        c = parse("areasize:200", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("areasize:100-200", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("areasize:-300", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("areasize:100-", false);
        Assert.assertTrue(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test for timestamp checking
     */
    @Test
    public void timestampTest() {
        TestMeta meta = new TestMeta();
        meta.timestamp = ElementTimestamp.parseDateTime("2005-10-01");
        Condition c = parse("timestamp:2004-1-5T14:00/2010", false);

        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("timestamp:2011/", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test for role in relations matching
     */
    @Test
    public void roleTest() {
        TestMeta meta = new TestMeta();
        meta.roles.add("stop");
        Condition c = parse("role:stop", false);

        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("role:false", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test for relations that have a specific role
     */
    @Test
    public void hasRoleTest() {
        TestMeta meta = new TestMeta();
        meta.hasRole = "stop";
        Condition c = parse("hasRole:stop", false);

        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("hasRole:platform", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test user id matching
     */
    @Test
    public void userTest() {
        TestMeta meta = new TestMeta();
        meta.user = "SimonPoole";
        Condition c = parse("user:SimonPoole", false);

        Assert.assertTrue(c.eval(Type.WAY, meta, null));

        c = parse("user:PooleSimon", false);
        Assert.assertFalse(c.eval(Type.WAY, meta, null));
    }

    /**
     * Test of grouping with parentheses
     */
    @Test
    public void parentheseTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("(test1 test2) | test3", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("(test1 | test2)  test", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("test1 (test2 | test)", false);
        System.err.println(c.toString());
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Check operator affinity
     */
    @Test
    public void affinityTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("test1 test2 | test3", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));
        c = parse("test1 test2 | test", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
        c = parse("test1 | test2 test", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Check logical not
     */
    @Test
    public void notTest() {
        Map<String, String> tags = new HashMap<>();
        tags.put("test1", "grrr");
        tags.put("test", "grrr");

        Condition c = parse("-test1", false);
        Assert.assertFalse(c.eval(Type.NODE, null, tags));

        c = parse("-test2", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));

        c = parse("-(test2 | test3)", false);
        Assert.assertTrue(c.eval(Type.NODE, null, tags));
    }

    /**
     * Check for selected element matching
     */
    @Test
    public void selectedTest() {
        TestMeta meta = new TestMeta();
        meta.selected = true;

        Condition c = parse("selected", false);
        Assert.assertTrue(c.eval(Type.NODE, meta, null));

        c = parse("-selected", false);
        Assert.assertFalse(c.eval(Type.NODE, meta, null));

        meta.selected = false;
        Assert.assertTrue(c.eval(Type.NODE, meta, null));
    }

    /**
     * Check for matching incompletely downloaded relations
     */
    @Test
    public void incompleteTest() {
        TestMeta meta = new TestMeta();
        meta.isIncomplete = true;

        Condition c = parse("incomplete", false);
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));

        c = parse("-incomplete", false);
        Assert.assertFalse(c.eval(Type.RELATION, meta, null));

        meta.isIncomplete = false;
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));
    }

    /**
     * Check for element in view box matching
     */
    @Test
    public void inViewTest() {
        TestMeta meta = new TestMeta();
        meta.isInview = true;

        Condition c = parse("inview", false);
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));

        c = parse("-inview", false);
        Assert.assertFalse(c.eval(Type.RELATION, meta, null));

        meta.isInview = false;
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));
    }

    /**
     * Check for all relation members in view matching
     */
    @Test
    public void allInViewTest() {
        TestMeta meta = new TestMeta();
        meta.isAllInview = true;

        Condition c = parse("allinview", false);
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));

        c = parse("-allinview", false);
        Assert.assertFalse(c.eval(Type.RELATION, meta, null));

        meta.isAllInview = false;
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));
    }

    /**
     * Check for in download area matching
     */
    @Test
    public void inDownloadArea() {
        TestMeta meta = new TestMeta();
        meta.isInDownloadedArea = true;

        Condition c = parse("indownloadedarea", false);
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));

        c = parse("-indownloadedarea", false);
        Assert.assertFalse(c.eval(Type.RELATION, meta, null));

        meta.isInDownloadedArea = false;
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));
    }

    /**
     * Check for all relation members in download area matching
     */
    @Test
    public void allInDownloadArea() {
        TestMeta meta = new TestMeta();
        meta.isAllInDownloadedArea = true;

        Condition c = parse("allindownloadedarea", false);
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));

        c = parse("-allindownloadedarea", false);
        Assert.assertFalse(c.eval(Type.RELATION, meta, null));

        meta.isAllInDownloadedArea = false;
        Assert.assertTrue(c.eval(Type.RELATION, meta, null));
    }

    /**
     * Check preset matching
     */
    @Test
    public void presetTest() {
        TestMeta meta = new TestMeta();
        meta.preset = "test1/test2";

        Condition c = parse("preset:\"test1/test2\"", false);
        Assert.assertTrue(c.eval(Type.NODE, meta, null));

        c = parse("preset:\"test1/test\"", false);
        Assert.assertFalse(c.eval(Type.NODE, meta, null));
    }

    /**
     * Parse a filter string and return the Condition object
     * 
     * @param filterString the filter string
     * @param regexp if true use regexps for tag matching
     * @return a Condition object
     */
    private Condition parse(@NotNull String filterString, boolean regexp) {

        try {
            JosmFilterParser parser = new JosmFilterParser(new ByteArrayInputStream(filterString.getBytes()));
            return parser.condition(regexp);
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
