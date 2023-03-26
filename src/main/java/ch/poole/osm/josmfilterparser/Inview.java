package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class Inview implements Condition {

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
}