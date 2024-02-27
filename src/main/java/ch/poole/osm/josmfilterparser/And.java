package ch.poole.osm.josmfilterparser;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class And implements Condition, LogicalOperator {
    final Condition c1;
    final Condition c2;

    /**
     * Logically AND two conditions
     * 
     * @param c1 first Condition
     * @param c2 second Condition
     */
    public And(@NotNull Condition c1, @NotNull Condition c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return c1.eval(type, meta, tags) && c2.eval(type, meta, tags);
    }

    @Override
    public void reset() {
        c1.reset();
        c2.reset();
    }

    @Override
    public String toString() {
        return c1.toString() + " " + c2.toString();
    }

    @Override
    public String toDebugString() {
        return c1.toDebugString() + " AND " + c2.toDebugString();
    }

    @Override
    public Condition toDNF() {
        Condition temp = c1;
        if (c1 instanceof Brackets) {
            temp = ((Brackets) c1).c;
        }
        temp = temp.toDNF();
        if (temp instanceof Or) {
            return new Or(new And(((Or) temp).c1, c2), new And(((Or) temp).c2, c2)).toDNF();
        }

        temp = c2;
        if (c2 instanceof Brackets) {
            temp = ((Brackets) c2).c;
        }
        temp = temp.toDNF();
        if (temp instanceof Or) {
            return new Or(new And(((Or) temp).c1, c1), new And(((Or) temp).c2, c1)).toDNF();
        }

        return new And(c1.toDNF(), c2.toDNF());
    }

    @Override
    public String toOverpass() {
        StringBuilder builder = new StringBuilder();
        builder.append(c1.toOverpass());
        builder.append(c2.toOverpass());
        return builder.toString();
    }
}
