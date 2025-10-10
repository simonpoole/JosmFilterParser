package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

public class Incomplete implements Condition, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isIncomplete();
    }

    @Override
    public String toString() {
        return "incomplete";
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}