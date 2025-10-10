package ch.poole.osm.josmfilterparser;

import java.io.Serializable;
import java.util.Map;

public class Version implements Condition, Serializable {

    private static final long serialVersionUID = 1L;

    final long version;

    /**
     * Create a Condition that checks if an element has the specified version
     * 
     * @param version the version
     */
    public Version(long version) {
        this.version = version;
    }

    @Override
    public boolean eval(Type type, Meta meta, Map<String, String> tags) {
        return version == meta.getVersion();
    }

    @Override
    public String toString() {
        return "version:" + Long.toString(version);
    }

    @Override
    public Condition toDNF() {
        return this;
    }

    @Override
    public String toOverpass() {
        return "(if:version() == " + version + ")";
    }
}
