package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class Closed implements Condition {

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isClosed();
    }

    @Override
    public String toString() {
        return "closed";
    }
}
