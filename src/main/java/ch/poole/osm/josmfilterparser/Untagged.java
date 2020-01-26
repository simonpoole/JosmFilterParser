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
}
