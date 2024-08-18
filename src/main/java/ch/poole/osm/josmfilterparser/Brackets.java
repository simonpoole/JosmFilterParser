package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Brackets implements Condition, LogicalOperator {
    
    private static final String RIGHT_BRACKET = ")";
    private static final String LEFT_BRACKET = "(";
    
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
    public void reset() {
        c.reset();
    }
    
    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c.eval(type, meta, tags);
    }
    
    @Override
    public Condition toDNF() {
        return c;
    }

    @Override
    public String toString() {
        return LEFT_BRACKET + c.toString() + RIGHT_BRACKET;
    }
    
    @Override
    public String toDebugString() {
        return LEFT_BRACKET + c.toDebugString() + RIGHT_BRACKET;
    }
}
