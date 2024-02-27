package ch.poole.osm.josmfilterparser;

import static ch.poole.osm.josmfilterparser.I18n.tr;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Not implements Condition, LogicalOperator {
    final Condition c1;

    /**
     * Logical NOT of a condition
     * 
     * @param c1 the Condition
     */
    public Not(@NotNull Condition c1) {
        this.c1 = c1;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return !c1.eval(type, meta, tags);
    }
    
    @Override
    public void reset() {
        c1.reset();
    }
    
    @Override
    public String toOverpass() {
        if (c1 instanceof Match) {
            Match match = new Match((Match) c1);
            match.negate();
            return match.toOverpass();
        }
        return "!" + c1.toOverpass();
    }

    @Override
    public String toString() {
        return "-" + c1.toString();
    }
    
    @Override
    public String toDebugString() {
        return "-" + c1.toDebugString();
    }

    @Override
    public Condition toDNF() {
        if (c1 instanceof Not) {
            return ((Not) c1).c1.toDNF();
        }
        if (c1 instanceof And) {
            return new Or(new Not(((And) c1).c1).toDNF(), new Not(((And) c1).c2).toDNF());
        }
        if (c1 instanceof Or) {
            return new And(new Not(((Or) c1).c1).toDNF(), new Not(((Or) c1).c2).toDNF());
        }
        if (c1 instanceof Brackets) {
            return new Not(((Brackets) c1).c).toDNF();
        }
        if (c1 instanceof LogicalOperator) {
            throw new UnsupportedOperationException(tr("conversion_from_to_dnf_not_supported", this.getClass().getSimpleName(), c1.getClass().getSimpleName()));
        }
        return this;
    }
}
