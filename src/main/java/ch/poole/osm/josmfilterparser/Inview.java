package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

public class Inview implements Condition, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isInview();
    }

    @Override
    public String toString() {
        return "inview";
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "({{bbox}})";
    }
}