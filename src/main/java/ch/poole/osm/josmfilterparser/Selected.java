package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

public class Selected implements Condition, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isSelected();
    }

    @Override
    public String toString() {
        return "selected";
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
