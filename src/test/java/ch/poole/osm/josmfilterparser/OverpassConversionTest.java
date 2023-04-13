
package ch.poole.osm.josmfilterparser;

import static org.junit.Assert.assertEquals;
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
 * Tests for conversion to DNF and then Overpass
 * 
 * @author Simon Poole
 *
 */
public class OverpassConversionTest {

    private final static String EOL = "\n";

    /**
     * Check logical not
     */
    @Test
    public void notTest1() {
        Condition c = parse("-test1:", false);
        assertTrue(c instanceof Not);
        assertTrue(((Not) c).c1 instanceof Match);
        c = c.toDNF();
        assertTrue(c instanceof Not);
        assertTrue(((Not) c).c1 instanceof Match);
        assertEquals("[!test1]", c.toOverpass());
    }

    /**
     * Check logical not
     */
    @Test
    public void notTest2() {
        Condition c = parse("-(test1=a | test2=b)", false);
        assertTrue(c instanceof Not);
        assertTrue(((Not) c).c1 instanceof Brackets);
        c = c.toDNF();
        assertTrue(c instanceof And);
        assertTrue(((And) c).c1 instanceof Not);
        assertTrue(((And) c).c2 instanceof Not);
        assertEquals("[test1!=a][test2!=b]", c.toOverpass());
    }

    /**
     * Check logical not
     */
    @Test
    public void notTest3() {
        Condition c = parse("-(test1=a | -test2=b)", false);
        assertTrue(c instanceof Not);
        assertTrue(((Not) c).c1 instanceof Brackets);
        c = c.toDNF();
        assertTrue(c instanceof And);
        assertTrue(((And) c).c1 instanceof Not);
        assertTrue(((And) c).c2 instanceof Match);
        assertEquals("[test1!=a][test2=b]", c.toOverpass());
    }

    /**
     * Check logical not
     */
    @Test
    public void notTest4() {
        Condition c = parse("-(test1=a test2=b)", false);
        assertTrue(c instanceof Not);
        assertTrue(((Not) c).c1 instanceof Brackets);
        dump(c, 0);
        c = c.toDNF();
        dump(c, 0);
        assertTrue(c instanceof Or);
        assertTrue(((Or) c).c1 instanceof Not);
        assertTrue(((Or) c).c2 instanceof Not);
        // @formatter:off
        assertEquals("(" + EOL + 
                "[test1!=a];" + EOL +
                "[test2!=b];" + EOL +
                ")" + EOL , c.toOverpass());
       // @formatter:on
    }

    /**
     * Check logical and
     */
    @Test
    public void andTest1() {
        Condition c = parse("(test1=a | test2=b) test3=bla type:way", false);
        dump(c, 0);
        assertTrue(c instanceof And);
        assertTrue(((And) c).c1 instanceof And);
        assertTrue(((And) c).c2 instanceof ElementType);
        c = c.toDNF();
        dump(c, 0);
        assertTrue(c instanceof Or);
        assertTrue(((Or) c).c1 instanceof And);
        assertTrue(((Or) c).c2 instanceof And);
        // @formatter:off
        assertEquals("(" + EOL + 
                "way[test1=a][test3=bla];" + EOL +
                "way[test2=b][test3=bla];" + EOL +
                ")" + EOL, c.toOverpass());
       // @formatter:on
    }

    /**
     * Check logical and
     */
    @Test
    public void andTest2() {
        Condition c = parse("test1=bla (test2=a | test3=b) type:node", false);
        dump(c, 0);
        c = c.toDNF();
        dump(c, 0);
        assertTrue(c instanceof Or);
        assertTrue(((Or) c).c1 instanceof And);
        assertTrue(((Or) c).c2 instanceof And);
        // @formatter:off
        assertEquals("(" + EOL + 
                "node[test2=a][test1=bla];" + EOL +
                "node[test3=b][test1=bla];" + EOL +
                ")" + EOL, c.toOverpass());
        // @formatter:on
    }

    /**
     * Check logical and
     */
    @Test
    public void andTest3() {
        Condition c = parse("(test1=a | test2=b) (test3=c | test4=d) type:relation", false);
        dump(c, 0);
        c = c.toDNF();
        dump(c, 0);
        assertTrue(c instanceof Or);
        assertTrue(((Or) c).c1 instanceof Or);
        assertTrue(((Or) c).c2 instanceof Or);
        // @formatter:off
        assertEquals("(" + EOL +
                "relation[test3=c][test1=a];" + EOL +
                "relation[test4=d][test1=a];" + EOL +
                "relation[test3=c][test2=b];" + EOL +
                "relation[test4=d][test2=b];" + EOL +
                ")" + EOL, c.toOverpass());
       // @formatter:on
    }

    /**
     * Check logical not
     */
    @Test
    public void xorTest() {
        Condition c = parse("(test1=a XOR test2=b) type:node", false);
        c = c.toDNF();
        assertTrue(c instanceof Or);
        assertTrue(((Or) c).c1 instanceof And);
        assertTrue(((Or) c).c2 instanceof And);
        // @formatter:off
        assertEquals("(" + EOL +
                "node[test1=a][test2!=b];" + EOL + 
                "node[test1!=a][test2=b];" + EOL +
                ")" + EOL, c.toOverpass());
        // @formatter:on
    }

    /**
     * Check propagation of element type
     */
    @Test
    public void elementTypeTest1() {
        Condition c = parse("test1=bla type:way", false);
        dump(c, 0);
        c = c.toDNF();
        dump(c, 0);
        assertEquals("[test1=bla]", c.toOverpass());
    }

    /**
     * Check propagation of element type
     */
    @Test
    public void elementTypeTest2() {
        Condition c = parse("type:node test1=bla", false);
        dump(c, 0);
        c = c.toDNF();
        dump(c, 0);
        assertEquals("[test1=bla]", c.toOverpass());
    }

    /**
     * Check that we get all three elements in an or
     */
    @Test
    public void elementTypeTest3() {
        Condition c = parse("test1=bla", false);
        dump(c, 0);
        c = c.toDNF();
        dump(c, 0);
        // @formatter:off
        assertEquals("(" + EOL +
                "node[test1=bla];" + EOL + 
                "way[test1=bla];" + EOL +
                "relation[test1=bla];" + EOL +
                ");" + EOL
                , Overpass.transform(c));
        // @formatter:on
    }

    /**
     * Check user
     */
    @Test
    public void userTest() {
        Condition c = parse("user:SimonPoole type:way", false);
        c = c.toDNF();
        assertEquals("(user:\"SimonPoole\")", c.toOverpass());
    }

    /**
     * Check closed
     */
    @Test
    public void closedTest() {
        Condition c = parse("closed type:way", false);
        c = c.toDNF();
        assertEquals("(if:is_closed() == 1)", c.toOverpass());
    }

    /**
     * Check id
     */
    @Test
    public void idTest() {
        Condition c = parse("id:0 type:way", false);
        c = c.toDNF();
        assertEquals("(0)", c.toOverpass());
    }

    /**
     * Check version
     */
    @Test
    public void versionTest() {
        Condition c = parse("version:1 type:way", false);
        c = c.toDNF();
        assertEquals("(if:version() == 1)", c.toOverpass());
    }

    /**
     * Check untagged
     */
    @Test
    public void untaggedTest() {
        Condition c = parse("untagged type:way", false);
        c = c.toDNF();
        assertEquals("(if:count_tags() == 0)", c.toOverpass());
    }

    /**
     * Check tags
     */
    @Test
    public void tagsTest1() {
        Condition c = parse("tags:1 type:way", false);
        c = c.toDNF();
        assertEquals("(if:count_tags() == 1)", c.toOverpass());
    }

    /**
     * Check tags
     */
    @Test
    public void tagsTest2() {
        Condition c = parse("tags:1-4 type:way", false);
        c = c.toDNF();
        assertEquals("(if:count_tags() >= 1)(if:count_tags() <= 4)", c.toOverpass());
    }

    /**
     * Check tags
     */
    @Test
    public void tagsTest3() {
        Condition c = parse("tags:1- type:way", false);
        c = c.toDNF();
        assertEquals("(if:count_tags() >= 1)", c.toOverpass());
    }

    /**
     * Check tags
     */
    @Test
    public void tagsTest4() {
        Condition c = parse("tags:-4 type:way", false);
        c = c.toDNF();
        assertEquals("(if:count_tags() <= 4)", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest1() {
        Condition c = parse("test:", false);
        c = c.toDNF();
        assertEquals("[test]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest2() {
        Condition c = parse("-test:", false);
        c = c.toDNF();
        assertEquals("[!test]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest3() {
        Condition c = parse("test=", false);
        c = c.toDNF();
        assertEquals("[test~\"^$\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest4() {
        Condition c = parse("-test=", false);
        c = c.toDNF();
        assertEquals("[test!~\"^$\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest5() {
        Condition c = parse("test~\"[a-z]*\"", false);
        c = c.toDNF();
        assertEquals("[test~\"[a-z]*\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest6() {
        Condition c = parse("-test~\"[a-z]*\"", false);
        c = c.toDNF();
        assertEquals("[test!~\"[a-z]*\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest7() {
        Condition c = parse("test=*", false);
        c = c.toDNF();
        assertEquals("[test~\".*\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest8() {
        Condition c = parse("-*=test", false);
        c = c.toDNF();
        assertEquals("[~\".*\"!~\"^test$\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest9() {
        Condition c = parse("*=test", false);
        c = c.toDNF();
        assertEquals("[~\".*\"~\"^test$\"]", c.toOverpass());
    }

    /**
     * Check match conversion
     */
    @Test
    public void matchTest10() {
        Condition c = parse("test?", false);
        c = c.toDNF();
        assertEquals("[test~\"^(true|yes|1|on)$\"]", c.toOverpass());
    }

    /**
     * Check in conversion
     */
    @Test
    public void in1() {
        Condition c = parse("highway=residential in \"Le Landeron\" type:way", false);
        c = c.toDNF();
        assertEquals("{{geocodeArea:Le Landeron}}->.Le_Landeron;\n" + "way[highway=residential](area.Le_Landeron);\n", Overpass.transform(c));
    }

    /**
     * Check around conversion
     */
    @Test
    public void around1() {
        Condition c = parse("highway=residential around \"Le Landeron\" type:way", false);
        c = c.toDNF();
        assertEquals("way[highway=residential](around:1000,{{geocodeCoords:Le Landeron}});\n", Overpass.transform(c));
    }

    /**
     * Check inview conversion
     */
    @Test
    public void inview1() {
        Condition c = parse("highway=residential inview type:way", false);
        c = c.toDNF();
        assertEquals("way[highway=residential]({{bbox}});\n", Overpass.transform(c));
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
            fail(pex.toString());
        } catch (Error err) {
            fail(err.toString());
        }
        return null;
    }

    /**
     * Output a textual representation of the hierarchical structure of the supplied Condition
     * 
     * @param c the Condition to display
     * @param depth the starting depth
     */
    private void dump(@NotNull Condition c, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
        System.out.println(c.getClass().getSimpleName());
        if ((c instanceof And)) {
            dump(((And) c).c1, depth + 1);
            dump(((And) c).c2, depth + 1);
        }
        if ((c instanceof Or)) {
            dump(((Or) c).c1, depth + 1);
            dump(((Or) c).c2, depth + 1);
        }
        if ((c instanceof Xor)) {
            dump(((Xor) c).c1, depth + 1);
            dump(((Xor) c).c2, depth + 1);
        }
        if (c instanceof Not) {
            dump(((Not) c).c1, depth + 1);
        }
        if (c instanceof Brackets) {
            dump(((Brackets) c).c, depth + 1);
        }
    }
}
