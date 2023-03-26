package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Brackets implements Condition, LogicalOperator {
    final Condition c;

    /**
     * "Bracket" this condition
     * 
     * @param c the Condition
     */
    public Brackets(@NotNull Condition c) {
        this.c = c;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return "(" + c.toString() + ")";
    }
}
