package ch.poole.osm.josmfilterparser;

import static ch.poole.osm.josmfilterparser.Overpass.CLOSE_BRACKET;
import static ch.poole.osm.josmfilterparser.Overpass.OPEN_BRACKET;
import static ch.poole.osm.josmfilterparser.Overpass.appendAsOverpass;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Or implements Condition, LogicalOperator {

    final Condition c1;
    final Condition c2;

    /**
     * Logically OR two conditions
     * 
     * @param c1 first Condition
     * @param c2 second Condition
     */
    public Or(@NotNull Condition c1, @NotNull Condition c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c1.eval(type, meta, tags) || c2.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return c1.toString() + " OR " + c2.toString();
    }

    @Override
    public Condition toDNF() {
        if (c1 instanceof LogicalOperator || c2 instanceof LogicalOperator) {
            return new Or(c1.toDNF(), c2.toDNF());
        }
        return this;
    }

    @Override
    public String toOverpass() {
        StringBuilder builder = new StringBuilder();
        builder.append(OPEN_BRACKET);
        orToOverpass(builder, this);
        builder.append(CLOSE_BRACKET);
        return builder.toString();
    }

    /**
     * Recursively descend a tree of Ors, outputing any other arguments
     * 
     * @param builder the StringBuilder to write to
     * @param or the Or condition
     */
    private void orToOverpass(@NotNull StringBuilder builder, @NotNull Or or) {
        if (or.c1 instanceof Or) {
            orToOverpass(builder, (Or) or.c1);
        } else {
            appendAsOverpass(builder, or.c1);
        }
        if (or.c2 instanceof Or) {
            orToOverpass(builder, (Or) or.c2);
        } else {
            appendAsOverpass(builder, or.c2);
        }
    }
}
