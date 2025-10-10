package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

public class Id implements Condition, Serializable {

    private static final long serialVersionUID = 1L;

    final long id;

    /**
     * Create a Condition that checks the id of an OSM element
     * 
     * @param id the id that should match
     */
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

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "(" + Long.toString(id) + ")";
    }
}
