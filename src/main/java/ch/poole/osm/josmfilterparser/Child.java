package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Child implements Condition, LogicalOperator, Serializable {

    private static final long serialVersionUID = 1L;

    private final Condition    c;
    private List<Serializable> parents = null;

    /**
     * Match if we are a child of an element for which Condition c is true
     * 
     * @param c the Condition
     */
    public Child(@NotNull Condition c) {
        this.c = c;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        if (parents == null) {
            parents = meta.getMatchingElements(getCondition());
        }
        return meta.isChild(type, meta, parents);
    }
    
    @Override
    public boolean debugEval(Type type, Meta meta, Map<String, String> tags) {
        if (parents == null) {
            parents = meta.getMatchingElements(c);
        }
        System.out.println("DEBUG CHILD " + parents.size() + " PARENTS");
        boolean r = meta.isChild(type, meta, parents);
        System.out.println("DEBUG CHILD " + r);
        return r;
    }

    @Override
    public void reset() {
        getCondition().reset();
        parents = null;
    }

    @Override
    public String toString() {
        return "child " + getCondition().toString();
    }

    @Override
    public String toDebugString() {
        return "child " + getCondition().toDebugString();
    }

    /**
     * @return the condition
     */
    @NotNull
    Condition getCondition() {
        return c;
    }
}