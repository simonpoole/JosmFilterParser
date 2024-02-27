package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Xor implements Condition, LogicalOperator {
    final Condition c1;
    final Condition c2;

    /**
     * Logically OR two conditions
     * 
     * @param c1 first Condition
     * @param c2 second Condition
     */
    public Xor(@NotNull Condition c1, @NotNull Condition c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public void reset() {
        c1.reset();
        c2.reset();
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c1.eval(type, meta, tags) ^ c2.eval(type, meta, tags);
    }

    @Override
    public String toString() {
        return c1.toString() + " XOR " + c2.toString();
    }

    @Override
    public String toDebugString() {
        return c1.toDebugString() + " XOR " + c2.toDebugString();
    }

    @Override
    public Condition toDNF() {
        return new Or(new And(c1, new Not(c2)), new And(new Not(c1), c2)).toDNF();
    }
}
