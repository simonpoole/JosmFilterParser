package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class Incomplete implements Condition {

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