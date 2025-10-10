package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

public class Changeset implements Condition, Serializable {

    private static final long serialVersionUID = 1L;

    final long changeset;

    /**
     * Check if an OSM element belongs to the specified changeset
     * 
     * @param changeset the changeset id
     */
    public Changeset(long changeset) {
        this.changeset = changeset;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return changeset == meta.getChangeset();
    }

    @Override
    public String toString() {
        return "changeset:" + Long.toString(changeset);
    }

    @Override
    public Condition toDNF() {
        return this;
    }
}
