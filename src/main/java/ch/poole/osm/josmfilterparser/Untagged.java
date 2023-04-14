package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class Untagged implements Condition {

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return tags == null || tags.isEmpty();
    }

    @Override
    public String toString() {
        return "untagged";
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "(if:count_tags() == 0)";
    }
}
