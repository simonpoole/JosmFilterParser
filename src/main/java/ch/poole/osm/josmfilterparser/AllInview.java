package ch.poole.osm.josmfilterparser;

import java.util.Map;

public class AllInview implements Condition {

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return meta.isAllInview();
    }

    @Override
    public String toString() {
        return "allinview";
    }
}
