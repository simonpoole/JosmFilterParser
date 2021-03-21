package ch.poole.osm.josmfilterparser;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Child implements Condition {
    final Condition c;
    List<Object> parents = null;
    
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
            parents = meta.getMatchingElements(c);
        }
        return meta.isChild(type, meta, parents);
    }

    @Override
    public String toString() {
        return "child " + c.toString();
    }
}
