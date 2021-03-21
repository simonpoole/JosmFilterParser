package ch.poole.osm.josmfilterparser;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

public class Parent implements Condition {
    final Condition c;
    List<Object>    children = null;

    /**
     * Match if we are a parent of an element for which Condition c is true
     * 
     * @param c the Condition
     */
    public Parent(@NotNull Condition c) {
        this.c = c;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        if (children == null) {
            children = meta.getMatchingElements(c);
        }
        return meta.isParent(type, meta, children);
    }

    @Override
    public String toString() {
        return "parent " + c.toString();
    }
}
