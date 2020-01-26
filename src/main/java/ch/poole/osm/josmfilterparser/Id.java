package ch.poole.osm.josmfilterparser;


import java.util.Map;

public class Id implements Condition {
    final long id;

    public Id(long id) {
        this.id = id;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return id == meta.getId();
    }

    @Override
    public String toString() {
        return "id:" + Long.toString(id);
    }
}
